#!/usr/local/bin/python
#coding:utf8
import struct
import traceback
from gevent.server import StreamServer
from gevent import monkey; monkey.patch_socket()
from gevent import Timeout

from message_pb2 import *
from cmd_type import CmdType

class OnlineClientManager(object):
    def __init__(self):
        self.clients = {}
        pass

    def add(self, client):
        self.clients[client.id] = client

    def get(self, id):
        return self.clients.get(id)

    def remove(self, client):
        del self.clients[client.id]

    def get_list(self, ex_cli):
        res = []
        for c in self.clients.values():
            if c == ex_cli:
                continue
            res.append(c)
            if len(res) > 5:
               break
        return res

        
class Client(object):
    FIGHT_REQ_A = 1
    FIGHT_REQ_B = FIGHT_REQ_A + 1
    WAIT_FOR_ANSWER = FIGHT_REQ_B  + 1
    START = WAIT_FOR_ANSWER  + 1
    WAIT_FOR_RESULT = START + 1
    def __init__(self, sk):
        global client_id
        self.sk = sk
        client_id += 1
        self.id = client_id
        self.state = 0
        self.cmd_buf = ''
        self.peer_client = None
        self.question_index = 0
        self.timeout = None
        self.right = 0
        pass

    def __del__(self):
        print "%d del" % self.id


    def start_question(self):
        self.question_index = 0
        self.right = 0
        self.last_right_choose = 0

    def send_msg(self, msg):
        self.sk.send(msg)

    def build_cmd(self, cmd_type, cmd):
        msg = cmd.SerializeToString()
        return struct.pack("!HH%ds" % len(msg), cmd_type, len(msg), msg)

    def lose_hb(self):
        if self.state == Client.FIGHT_REQ_A or self.state == Client.FIGHT_REQ_B:
            self.state = 0
            self.peer_client.state = 0

            self.cancel_timeout()
            cmd = OFightResp()
            cmd.result = 1
            cmd.message = 'other lose connection'
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client = None
            self.peer_client.peer_client = None
        elif self.state == Client.WAIT_FOR_ANSWER or self.state == Client.WAIT_FOR_RESULT:
            self.state = 0
            self.peer_client.state = 0

            self.cancel_timeout()
            cmd = OFightResult()
            cmd.result = 3
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client = None
            self.peer_client.peer_client = None

            

    def deal_timeout(self):
        self.timeout = None
        if self.state == Client.FIGHT_REQ_A:
            self.state = 0
            self.peer_client.state = 0

            cmd = OFightResp()
            cmd.result = 1
            cmd.message = "timeout"
            self.timeout = None
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))
            self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client.peer_client = None
            self.peer_client = None
        elif self.state == Client.WAIT_FOR_ANSWER:
            self.state = 0
            self.peer_client.state = 0

            cmd = OFightResult()
            cmd.result = 1
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, cmd))
            cmd.result = 2
            self.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, cmd))

            self.peer_client.peer_client = None
            self.peer_client = None
        pass

    def read_and_deal_cmd(self):
        while len(self.cmd_buf) < 4:
            self.cmd_buf += self.sk.recv(4 - len(self.cmd_buf)) # 2 is cmd type and 2 is protobuf string

        cmd_type, cmd_len = struct.unpack("!HH", self.cmd_buf)
        while len(self.cmd_buf) < cmd_len + 4:
            self.cmd_buf += self.sk.recv(cmd_len + 4 - len(self.cmd_buf))
        self.deal_cmd(cmd_type, self.cmd_buf[4:])
        self.cmd_buf = ''

    def start_timeout(self, seconds):
        self.cancel_timeout()
        self.timeout = Timeout(seconds)
        self.timeout.start()

    def cancel_timeout(self):
        if self.timeout:
            self.timeout.cancel()
            self.timeout = None
        

    def get_question(self):
        resp = OQuestion()
        resp.statement = 'haha'
        resp.index = self.question_index
        self.question_index += 1
        resp.all = 5
        resp.options.append('a')
        resp.options.append('b')
        resp.options.append('c')
        resp.options.append('d')
        resp.right = 0
        return resp

    def deal_cmd(self, cmd_type, buf):
        print cmd_type, self.state
        if cmd_type == CmdType.HEARTBEAT:
            return
        elif cmd_type == CmdType.FETCH_PEER_LIST_REQ:
            resp = OPeerListResp()
            clients = client_mgr.get_list(self)
            for c in clients:
                u = resp.users.add()
                u.id = c.id

            self.send_msg(self.build_cmd(CmdType.FETCH_PEER_LIST_RESP, resp))
        elif cmd_type == CmdType.FIGHT_REQ and self.state == 0:
            req = IFightReq()
            req.ParseFromString(buf)
            client = client_mgr.get(req.id)
            if client:
                self.state = Client.FIGHT_REQ_A
                resp = OFightReq()
                resp.user.id = client.id
                client.send_msg(self.build_cmd(CmdType.FIGHT_REQ, resp))
                client.state = Client.FIGHT_REQ_B
                self.peer_client = client
                client.peer_client = self
                self.start_timeout(2 * 60)
            else:
                resp = OFightResp()
                resp.result = 1
                resp.message = 'no such user'
                self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, resp))
        elif cmd_type == CmdType.FIGHT_RESP and self.state == Client.FIGHT_REQ_B:
            req = IFightResp()
            req.ParseFromString(buf)
            self.peer_client.cancel_timeout()
            if req.result == 0:
                self.start_question()
                self.peer_client.start_question()
                self.peer_client.send_msg(self.build_cmd(CmdType.QUESTION, self.peer_client.get_question()))
                self.send_msg(self.build_cmd(CmdType.QUESTION, self.get_question()))
                self.state = self.peer_client.state = Client.WAIT_FOR_ANSWER
                self.start_timeout(2 * 60)
            else:
                resp = OFightResp()
                resp.result = 1
                resp.message = 'other does not agree'
                self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, resp))
                self.state = 0
                self.peer_client.state = 0

        elif cmd_type == CmdType.ANSWER and self.state == Client.WAIT_FOR_ANSWER:
            self.cancel_timeout()
            req = IAnswer()
            req.ParseFromString(buf)
            if req.index != self.question_index - 1:
                return
            cmd = OFightState()
            cmd.all = 5
            cmd.done = self.question_index
            if req.choose == self.last_right_choose:
                self.right += 1
            cmd.right = self.right
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_STATE, cmd))
            if req.index >= 4:
                if self.peer_client.state == Client.WAIT_FOR_RESULT:
                    resp = OFightResult()
                    resp.result = 1
                    self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, resp))
                    resp.result = 2
                    self.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, resp))
                    self.state = 0
                    self.peer_client.state = 0
                else:
                    self.state = Client.WAIT_FOR_RESULT
            else:
                self.start_timeout(2 * 60)
                self.send_msg(self.build_cmd(CmdType.QUESTION, self.get_question()))
                

client_id = 0
client_mgr = OnlineClientManager()

def main(socket, address):
    global client_mgr
    print "one client", address
    client = Client(socket)
    client_mgr.add(client)
    hbTimer = None
    while True:
        try:
            hbTimer = Timeout(60)
            hbTimer.start()
            client.read_and_deal_cmd()
            hbTimer.cancel()
        except Timeout, t:
            if t == hbTimer:
                print "client lose"
                client.lose_hb()
                client_mgr.remove(client)
                client = None
                break
            else:
                print "other timeout"
                hbTimer.cancel()
                client.deal_timeout()

        except:
            traceback.print_exc()
            print "client close"
            client.lose_hb()
            client_mgr.remove(client)
            client = None
            


if __name__ == '__main__':
    server = StreamServer(('0.0.0.0', 9300), main)
    server.serve_forever()
