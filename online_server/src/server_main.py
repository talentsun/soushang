#!/usr/local/bin/python
#coding:utf8
import struct
import traceback
import logging
import time
import urllib2

from gevent.server import StreamServer
from gevent import monkey; monkey.patch_socket()
from gevent import Timeout

from message_pb2 import *
from cmd_type import CmdType

from utils import InitLogger
from google.protobuf.internal import decoder
from google.protobuf.internal import encoder

from lbs import LBSClientManager
from user import UserInfo

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
    INIT_INFO = 0
    IDLE = INIT_INFO
    FIGHT_REQ_A = IDLE + 1
    FIGHT_REQ_B = FIGHT_REQ_A + 1
    WAIT_FOR_ANSWER = FIGHT_REQ_B  + 1
    START = WAIT_FOR_ANSWER  + 1
    WAIT_FOR_RESULT = START + 1
    def __init__(self, sk):
        global client_id
        self.sk = sk
        self.id = -1
        self.name = ''
        self.state = Client.INIT_INFO
        self.cmd_buf = ''
        self.peer_client = None
        self.timeout = None
        self.longitude = None
        self.latitude = None

        self.question_index = 0
        self.start_time = 0
        self.end_time = 0
        self.bet = 0
        self.right = 0
        self.last_answer_time = 0
        self.today_bet_num = 0
        self.last_bet_time = 0

        self.visible = False
        pass

    def __del__(self):
        print "%d del" % self.id


    def send_msg(self, msg):
        logger.debug("%d send msg len %d", self.id, len(msg))
        self.sk.send(msg)

    def build_cmd(self, cmd_type, cmd):
        msg = cmd.SerializeToString()
        cmd = CommandMsg()
        cmd.type = cmd_type
        cmd.content = msg
        msg = cmd.SerializeToString()
        
        class PrefixWriter(object):
            def __init__(self):
                self.msg = ''
            def write(self, onechr):
                self.msg = onechr + self.msg


        writer = PrefixWriter()
        encoder._EncodeVarint(writer.write, len(msg))
        return writer.msg + msg


    def lose_hb(self):
        logger.debug("%d lose hb" % self.id)
        if self.state == Client.FIGHT_REQ_A or self.state == Client.FIGHT_REQ_B:

            self.cancel_timeout()
            cmd = OFightResp()
            cmd.result = 1
            cmd.message = 'other lose connection'
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))
            self.end_game()

        elif self.state == Client.WAIT_FOR_ANSWER or self.state == Client.WAIT_FOR_RESULT:

            self.cancel_timeout()
            cmd = OFightResult()
            cmd.result = 1
            self.peer_client.user_info.win_num += 1
            self.peer_client.user_info.fight_num += 1
            self.user_info.fight_num += 1
            self.user_info.store()
            self.peer_client.user_info.store()

            cmd.me_win_ratio = float(self.peer_client.user_info.win_num) / self.peer_client.user_info.fight_num
            cmd.me_score = (self.right + self.peer_client.right) * 5 + self.bet
            cmd.me_point = self.right * 5
            cmd.other_point = self.peer_client.right * 5
            cmd.other_time = int(time.time() - self.start_time)
            cmd.me_time = int(time.time() - self.peer_client.start_time)
            other_score = -1 * self.bet
            if cmd.me_score != 0:
                urllib2.urlopen("http://soushang.limijiaoyin.com/index.php/Devent/addScore.html?uid=%d&score=%d" % (self.peer_client.id, cmd.me_score))
            if other_score != 0:
                urllib2.urlopen("http://soushang.limijiaoyin.com/index.php/Devent/addScore.html?uid=%d&score=%d" % (self.id, other_score))

            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))
            self.end_game()


    def deal_timeout(self):
        logger.debug("%d timeout %d" % (self.id, self.state))
        self.timeout = None
        if self.state == Client.FIGHT_REQ_A:

            cmd = OFightResp()
            cmd.result = 1
            cmd.message = "timeout"
            self.timeout = None
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))
            self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, cmd))

            self.end_game()

        elif self.state == Client.WAIT_FOR_ANSWER:
            self.check_answer_timeout()
        pass

    def read_and_deal_cmd(self):
        while True:
            tmpBuf = self.sk.recv(1) # 4 is protobuf string
            if len(tmpBuf) == 0:
                raise Exception, 'close'
            self.cmd_buf += tmpBuf
            if ord(tmpBuf[0]) & 0x80 == 0:
                break

        cmd_len = decoder._DecodeVarint32(self.cmd_buf, 0)[0]
        prefix_len = len(self.cmd_buf)
        print "cmd len", cmd_len
        while len(self.cmd_buf) < cmd_len + prefix_len:
            tmpBuf = self.sk.recv(cmd_len + prefix_len - len(self.cmd_buf))
            if len(tmpBuf) == 0:
                raise Exception, 'close'
            self.cmd_buf += tmpBuf
        cmd = CommandMsg()
        cmd.ParseFromString(self.cmd_buf[prefix_len:])
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
        
    def deal_fight_result(self, me_win):
        logger.debug("deal fight result %d" % me_win)
        #me
        resp = OFightResult()
        if me_win:
            resp.result = 1
            self.user_info.win_num += 1
            self.user_info.fight_num += 1
            self.peer_client.user_info.fight_num += 1
        else:
            resp.result = 2
            self.user_info.fight_num += 1
            self.peer_client.user_info.win_num += 1
            self.peer_client.user_info.fight_num += 1
        self.user_info.store()
        self.peer_client.user_info.store()
        logger.debug("%d right %d %d right %d bet %d" % (self.id, self.right, self.peer_client.id, self.peer_client.right, self.bet))

        resp.me_win_ratio = float(self.user_info.win_num) / self.user_info.fight_num
        resp.me_point = self.right * 5;
        resp.other_point = self.peer_client.right * 5;
        if me_win:
            resp.me_score = (self.right + self.peer_client.right) * 5 + self.bet
        else:
            resp.me_score = -1 * self.bet
        resp.other_time = int(self.peer_client.end_time - self.peer_client.start_time)
        resp.me_time = int(self.end_time - self.start_time)
        self.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, resp))
        urllib2.urlopen("http://soushang.limijiaoyin.com/index.php/Devent/addScore.html?uid=%d&score=%d" % (self.id, resp.me_score))

        #other
        if not me_win:
            resp.result = 1
        else:
            resp.result = 2

        resp.me_win_ratio = float(self.peer_client.user_info.win_num) / self.peer_client.user_info.fight_num
        if not me_win:
            resp.me_score = (self.right + self.peer_client.right) * 5 + self.bet
        else:
            resp.me_score = -1 * self.bet


        resp.other_point = self.right * 5;
        resp.me_point = self.peer_client.right * 5;
        resp.me_time = int(self.peer_client.end_time - self.peer_client.start_time)
        resp.other_time = int(self.end_time - self.start_time)

        urllib2.urlopen("http://soushang.limijiaoyin.com/index.php/Devent/addScore.html?uid=%d&score=%d" % (self.peer_client.id, resp.me_score))
        self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_RESULT, resp))
        self.end_game()

    def end_game(self):
        if self.peer_client:
            self.peer_client.state = Client.IDLE
            self.peer_client.peer_client = None
            self.peer_client.cancel_timeout()
        self.state = Client.IDLE
        self.peer_client = None
        self.cancel_timeout()

    def check_answer_timeout(self):
        cur_time = time.time()
        #because the time may be not exact
        if self.last_answer_time <= cur_time - ONE_MOVE_MAX_TIME + 1:
            self.deal_fight_result(0)
            return True
        if self.peer_client.last_answer_time <= cur_time - ONE_MOVE_MAX_TIME + 1:
            self.deal_fight_result(1)
            return True
        return False



    def deal_cmd(self, cmd_type, buf):
        logger.debug("receive cmd type %d state %d", cmd_type, self.state)
        if cmd_type == CmdType.HEARTBEAT:
            return
        if self.id == -1:
            if cmd_type == CmdType.CLIENT_INFO:
                cmd = IClientInfo()
                cmd.ParseFromString(buf)
                if client_mgr.get(cmd.id):
                    logger.error("id %d already in system" % cmd.id)
                    self.send_msg(self.build_cmd(CmdType.LOGIN_FAIL, EmptyMsg()))
                    return
                self.name = cmd.name
                self.id = cmd.id
                self.net_type = cmd.net_type
                self.avatar = cmd.avatar
                self.user_info = UserInfo.get_user_by_id(self.id)
                if not self.user_info:
                    self.user_info = UserInfo()
                    self.user_info.id = self.id
                logger.debug("client name %s", cmd.name)
                self.send_msg(self.build_cmd(CmdType.LOGIN_SUCC, EmptyMsg()))
                return
            else:
                logger.debug("client not set client info")
                self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                return

        if cmd_type == CmdType.ON_LINE:
            self.visible = True
        elif cmd_type == CmdType.OFF_LINE:
            self.visible = False
        elif cmd_type == CmdType.CLIENT_LBS:
            cmd = IClientLBS()
            cmd.ParseFromString(buf)
            if self.latitude != None:
                client_mgr.remove_client(self)
            if client_mgr.get(self.id):
                logger.error("id %d already in system" % self.id)
                self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                return
            self.latitude = cmd.latitude
            self.longitude = cmd.longitude
            client_mgr.add_client(self)
            logger.debug("client lbs lat %f long %f" % (cmd.latitude, cmd.longitude))
        elif cmd_type == CmdType.FETCH_PEER_LIST_REQ:
            if self.latitude == None:
                self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                logger.debug("client not set the lbs info")
                return
            resp = OPeerListResp()
            logger.debug("client fetch peer list")
            clients = client_mgr.get_near(self, 5)
            for c in clients:
                if not c.visible:
                    continue
                u = resp.users.add()
                u.name = c.name
                u.id = c.id
                u.fight_num = c.user_info.fight_num
                u.win_num = c.user_info.win_num
                u.avatar = c.avatar
                u.net_type = c.net_type
                u.state = c.state

            self.send_msg(self.build_cmd(CmdType.FETCH_PEER_LIST_RESP, resp))
        elif cmd_type == CmdType.FIGHT_REQ and self.state == Client.IDLE:
            req = IFightReq()
            req.ParseFromString(buf)
            today_begin_time = int((time.time()  + 8 * 3600) / (24 * 3600)) * 24 * 3600 - 8 * 3600
            if self.last_bet_time < today_begin_time:
                self.bet_num = 0
            if req.bet > 0 and self.bet_num >= 5:
                resp = OFightResp()
                resp.result = 2
                resp.message = 'bet too much'
                logger.error("bet too much %d" % self.id)
                self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, resp))
                return

            logger.debug("client fight req %d" % req.id)
            client = client_mgr.get(req.id)
            if client:
                if client.state != Client.IDLE:
                    resp = OFightResp()
                    resp.result = 1
                    resp.message = u'other is busy'
                    self.send_msg(self.build_cmd(CmdType.FIGHT_RESP, resp))
                    return
                self.state = Client.FIGHT_REQ_A
                self.bet = req.bet
                client.bet = req.bet
                resp = OFightReq()
                resp.user.id = self.id
                resp.user.name = self.name
                resp.user.avatar = self.avatar
                resp.user.net_type = self.net_type
                resp.user.fight_num = self.user_info.fight_num
                resp.user.win_num = self.user_info.win_num
                resp.user.state = self.state
                resp.bet = req.bet
                client.send_msg(self.build_cmd(CmdType.FIGHT_REQ, resp))
                client.state = Client.FIGHT_REQ_B
                self.peer_client = client
                client.peer_client = self
                self.start_timeout(ONE_MOVE_MAX_TIME)
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
                question = OQuestion()
                question.fight_key = "%d_%d_%d" % (self.peer_client.id, self.id, int(time.time()))
                self.peer_client.send_msg(self.build_cmd(CmdType.QUESTION, question))
                self.send_msg(self.build_cmd(CmdType.QUESTION, question))
                self.state = self.peer_client.state = Client.WAIT_FOR_ANSWER
                self.start_timeout(ONE_MOVE_MAX_TIME)
                self.question_index = self.peer_client.question_index = 0
                self.start_time = self.peer_client.start_time = time.time()
                self.peer_client.last_answer_time = self.last_answer_time = time.time()
                self.right = self.peer_client.right = 0
                #start game
                if self.bet > 0:
                    self.peer_client.bet_num += 1
                    self.peer_client.last_bet_time = time.time()
                    logger.debug("%d bet num %d %d" % (self.peer_client.id, self.peer_client.bet_num, int(self.peer_client.last_bet_time)))
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
            logger.debug("%d fight cancel" % self.id)
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_CANCEL, EmptyMsg()))
            self.peer_client.state = self.state = Client.IDLE
            self.peer_client.peer_client = None
            self.peer_client = None

        elif cmd_type == CmdType.ANSWER and self.state == Client.WAIT_FOR_ANSWER:
            self.cancel_timeout()
            self.last_answer_time = time.time()
            if self.check_answer_timeout():
                #timeout end of game
                return
            req = IAnswer()
            req.ParseFromString(buf)
            if req.index != self.question_index:
                logger.error("answer is not right index")
                return
            self.question_index += 1
            logger.debug("%d done %d" % (self.id, self.question_index))
            cmd = OFightState()
            cmd.all = 10
            cmd.done = self.question_index
            if req.right:
                self.right += 1
            cmd.right = self.right
            self.peer_client.send_msg(self.build_cmd(CmdType.FIGHT_STATE, cmd))
            if req.index >= QUESTION_NUM_PER_GAME - 1:
                logger.debug("%d finish" % self.id)
                self.end_time = time.time()
                if self.peer_client.state == Client.WAIT_FOR_RESULT:
                    self.deal_fight_result(self.right > self.peer_client.right)
                else:
                    self.state = Client.WAIT_FOR_RESULT
            else:
                self.start_timeout(ONE_MOVE_MAX_TIME)
        elif cmd_type == CmdType.FIGHT_QUIT and (self.state == Client.WAIT_FOR_ANSWER or self.state == Client.WAIT_FOR_RESULT):
            logger.debug("%d quit" % self.id)
            self.end_time = self.peer_client.end_time = time.time()
            self.deal_fight_result(0)
        else:
            self.send_msg(self.build_cmd(CmdType.UNKNOWN_OP, EmptyMsg()))
                

client_id = 0
client_mgr = LBSClientManager()
QUESTION_NUM_PER_GAME = 10
ONE_MOVE_MAX_TIME = 120

def main(socket, address):
    global client_mgr
    print "one client", address
    logger.debug("one client %s" % str(address))
    client = Client(socket)
    hbTimer = None
    while True:
        try:
            hbTimer = Timeout(ONE_MOVE_MAX_TIME)
            hbTimer.start()
            client.read_and_deal_cmd()
            hbTimer.cancel()
        except Timeout, t:
            if t == hbTimer:
                print "client lose"
                client.lose_hb()
                client.cancel_timeout()
                if client.latitude != None:
                    client_mgr.remove_client(client)
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
            client.cancel_timeout()
            if client.latitude != None:
                client_mgr.remove_client(client)
            client = None
            break
            


if __name__ == '__main__':
    server = StreamServer(('0.0.0.0', 9300), main)
    server.serve_forever()
