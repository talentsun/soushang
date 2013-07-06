#!/usr/local/bin/python
#coding:utf8
import struct
import traceback
import logging

from gevent.server import StreamServer
from gevent import monkey; monkey.patch_socket()
from gevent import Timeout

from message_pb2 import *
from cmd_type import CmdType

from utils import InitLogger

logger = InitLogger("server_main", logging.DEBUG, "../log/server_main.log")

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
            if c.state != 0:
                continue
            res.append(c)
            if len(res) > 5:
               break
        return res

        
class Client(object):
    IDLE = 0
    FIGHT_REQ_A = IDLE + 1
    FIGHT_REQ_B = FIGHT_REQ_A + 1
    WAIT_FOR_ANSWER = FIGHT_REQ_B  + 1
    START = WAIT_FOR_ANSWER  + 1
    WAIT_FOR_RESULT = START + 1
    def __init__(self, sk):
        global client_id
        self.sk = sk
        client_id += 1
        self.id = client_id
        self.name = ''
        self.state = Client.IDLE
        self.cmd_buf = ''
        self.peer_client = None
        self.question_index = 0
        self.timeout = None
        self.right = 0
        self.longitude = None
        self.latitude = None
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
        cmd = CommandMsg()
        cmd.type = cmd_type
        cmd.content = msg
        msg = cmd.SerializeToString()

        return struct.pack("!I%ds" % len(msg), len(msg), msg)

    def lose_hb(self):
        logger.debug("%d lose hb" % self.id)
        if self.state == Client.FIGHT_REQ_A or self.state == Client.FIGHT_REQ_B:
            self.state = Client.IDLE
            self.peer_client.state = Client.IDLE

            self.cancel_timeout()
            cmd = OFightResp()
            cmd.result = 1
            cmd.message = 'other lose connection'
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client.peer_client = None
            self.peer_client = None
        elif self.state == Client.WAIT_FOR_ANSWER or self.state == Client.WAIT_FOR_RESULT:
            self.state = Client.IDLE
            self.peer_client.state = Client.IDLE

            self.cancel_timeout()
            cmd = OFightResult()
            cmd.result = 3
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client.peer_client = None
            self.peer_client = None

            

    def deal_timeout(self):
        logger.debug("%d timeout %d" % (self.id, self.state))
        self.timeout = None
        if self.state == Client.FIGHT_REQ_A:
            self.state = Client.IDLE
            self.peer_client.state = Client.IDLE

            cmd = OFightResp()
            cmd.result = 1
            cmd.message = "timeout"
            self.timeout = None
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))
            self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.peer_client.peer_client = None
            self.peer_client = None
        elif self.state == Client.WAIT_FOR_ANSWER:
            self.state = Client.IDLE
            self.peer_client.state = Client.IDLE

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
            tmpBuf = self.sk.recv(4 - len(self.cmd_buf)) # 4 is protobuf string
            if len(tmpBuf) == 0:
                raise Exception, 'close'
            self.cmd_buf += tmpBuf

        cmd_len = struct.unpack("!I", self.cmd_buf)[0]
        print "cmd len", cmd_len
        while len(self.cmd_buf) < cmd_len + 4:
            tmpBuf = self.sk.recv(cmd_len + 4 - len(self.cmd_buf))
            if len(tmpBuf) == 0:
                raise Exception, 'close'
            self.cmd_buf += tmpBuf
        cmd = CommandMsg()
        cmd.ParseFromString(self.cmd_buf[4:])
        print "receive cmd", cmd.type
        self.deal_cmd(cmd.type, cmd.content)
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
        logger.debug("receive cmd type %d state %d", cmd_type, self.state)
        if cmd_type == CmdType.HEARTBEAT:
            return
        if len(self.name) == 0:
            if cmd_type == CmdType.CLIENT_INFO:
                cmd = IClientInfo()
                cmd.ParseFromString(buf)
                self.name = cmd.name
                logger.debug("client name %s", cmd.name)
                return
            else:
                logger.debug("client not set client info")
                self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                return

        if cmd_type == CmdType.CLIENT_LBS:
            cmd = IClientLBS()
            cmd.ParseFromString(buf)
            self.latitude = cmd.latitude
            self.longitude = cmd.longitude
            logger.debug("client lbs lat %f long %f" % (cmd.latitude, cmd.longitude))
        elif cmd_type == CmdType.FETCH_PEER_LIST_REQ:
            if self.latitude == None:
                self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                logger.debug("client not set the lbs info")
                return
            resp = OPeerListResp()
            logger.debug("client fetch peer list")
            clients = client_mgr.get_list(self)
            for c in clients:
                u = resp.users.add()
                u.name = c.name
                u.id = c.id

            self.send_msg(self.build_cmd(CmdType.FETCH_PEER_LIST_RESP, resp))
        elif cmd_type == CmdType.FIGHT_REQ and self.state == Client.IDLE:
            req = IFightReq()
            req.ParseFromString(buf)
            logger.debug("client fight req %d" % req.id)
            client = client_mgr.get(req.id)
            if client:
                self.state = Client.FIGHT_REQ_A
                resp = OFightReq()
                resp.user.id = self.id
                resp.user.name = self.name
                client.send_msg(self.build_cmd(CmdType.FIGHT_REQ, resp))
                client.state = Client.FIGHT_REQ_B
                self.peer_client = client
                client.peer_client = self
                self.start_timeout(2 * 60)
            else:
                resp = OFightResp()
                resp.result = 1
                resp.message = 'no such user'
                logger.error("not find user %d" % req.id)
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
                logger.debug("peer %d disagree")
                self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, resp))
                self.state = Client.IDLE
                self.peer_client.state = Client.IDLE
                self.peer_client.peer_client = None
                self.peer_client = None

        elif cmd_type == CmdType.FIGHT_CANCEL and (self.state == Client.FIGHT_REQ_A or self.state == Client.WAIT_FOR_ANSWER):
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_CANCEL, EmptyMsg()))
            self.peer_client.state = self.state = Client.IDLE
            self.peer_client.peer_client = None
            self.peer_client = None

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
                    self.state = Client.IDLE
                    self.peer_client.state = Client.IDLE
                else:
                    self.state = Client.WAIT_FOR_RESULT
            else:
                self.start_timeout(2 * 60)
                self.send_msg(self.build_cmd(CmdType.QUESTION, self.get_question()))
        else:
            self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                

client_id = 0
client_mgr = OnlineClientManager()

def main(socket, address):
    global client_mgr
    print "one client", address
    logger.debug("one client %s" % str(address))
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
            hbTimer.cancel()
            traceback.print_exc()
            logger.error(traceback.format_exc())
            print "client close"
            client.lose_hb()
            client_mgr.remove(client)
            client = None
            break
            


if __name__ == '__main__':
    server = StreamServer(('0.0.0.0', 9300), main)
    server.serve_forever()
