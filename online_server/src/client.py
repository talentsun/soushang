#!/usr/local/bin/python
#coding:utf-8
from message_pb2 import *
from cmd_type import CmdType
from socket import socket
import struct
import time
import threading
import random

import urllib2, cookielib
import urllib, json

from google.protobuf.internal import decoder
from google.protobuf.internal import encoder

class User(object):
    def __init__(self):
        self.sk = None
        cj = cookielib.CookieJar()
        opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
        urllib2.install_opener(opener)
        self.fight_key = 'jfwang213'
        self.question_id = 0
        pass

    def connect(self):
        if self.sk:
            return
        self.sk = socket()
        self.sk.connect( ('118.244.225.222', 9300) )

    def send_heartbeat(self):
        while not self.sk:
            time.sleep(1)


        while True:
            self.send_cmd(CmdType.HEARTBEAT, EmptyMsg())
            time.sleep(30)

    def send_cmd(self, cmd_type, cmd):
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
        print len(writer.msg)
        self.sk.send(writer.msg + msg)
        
    def sendFetchList(self):
        self.send_cmd(CmdType.FETCH_PEER_LIST_REQ, EmptyMsg())

    def sendFightReq(self):
        id = int(raw_input("other's id:"))
        cmd = IFightReq()
        cmd.id = id
        cmd.bet = 5
        self.send_cmd(CmdType.FIGHT_REQ, cmd)

    def sendFightCancel(self):
        self.send_cmd(CmdType.FIGHT_CANCEL, EmptyMsg())

    def sendFightResp(self):
        print "0 show agree others disgree"
        id = int(raw_input(""))
        cmd = IFightResp()
        cmd.result = id
        self.send_cmd(CmdType.FIGHT_RESP, cmd)


    def sendClientInfo(self):
        name = raw_input("your name is:")
        if len(name) == 0:
            return
        cmd = IClientInfo()
        cmd.name = name
        cmd.id = random.randint(1, 10000000)
        cmd.avatar = 'avatar'
        cmd.net_type = 1
        self.send_cmd(CmdType.CLIENT_INFO, cmd)

    def sendLBS(self):
        longitude = int(raw_input("longitude:"))
        latitude = int(raw_input("latitude:"))
        cmd = IClientLBS()
        cmd.longitude = longitude
        cmd.latitude = latitude
        self.send_cmd(CmdType.CLIENT_LBS, cmd)

    def sendAnswer(self):
        i = int(raw_input("your choices is:"))
        index = int(raw_input("question index is:"))
        cmd = IAnswer()
        cmd.index = index
        cmd.choose = i
        self.send_cmd(CmdType.ANSWER, cmd)

    def fetchQuestion(self):
        data = urllib.urlencode({'udid':'aaa', 's':'%d' % (self.question_id), 'type': 'lbs_fight', 'fight_key': self.fight_key})

        resp = urllib2.urlopen("http://soushang.limijiaoyin.com/index.php/Devent/next.html", data)
        result = json.loads(resp.read())
        print result
        self.question_id = int(result['question']['question_id'])
        self.question_index = int(result['question']['index'])
        print "id %d index %d" % (self.question_id, self.question_index)
        cmd = IAnswer()
        cmd.index = self.question_index
        cmd.right = random.randint(0, 1)
        self.send_cmd(CmdType.ANSWER, cmd)


    def showFetchList(self, buf):
        cmd = OPeerListResp()
        cmd.ParseFromString(buf)
        for i in cmd.users:
            print "one user", i.id, i.name

    def showFightReq(self, buf):
        cmd = OFightReq()
        cmd.ParseFromString(buf)
        print "fight req from", cmd.user.id

    def showFightResp(self, buf):
        cmd = OFightResp()
        cmd.ParseFromString(buf)
        print cmd.result
        print cmd.message


    def showQuestion(self, buf):
        cmd = OQuestion()
        cmd.ParseFromString(buf)
        self.fight_key = cmd.fight_key
        print self.fight_key
        self.question_id = 0

    def showResult(self, buf):
        cmd = OFightResult()
        cmd.ParseFromString(buf)
        print "1 is win 2 is lose, your result is", cmd.result
        print cmd.me_win_ratio
        print cmd.other_win_ratio
        print cmd.me_score
        print cmd.other_score
        print cmd.other_time_cost

    def showFightState(self, buf):
        cmd = OFightState()
        cmd.ParseFromString(buf)
        print "all:", cmd.all
        print "done:", cmd.done
        print "right:", cmd.right

    def showResp(self):
        while self.sk == None:
            time.sleep(1)
                
        while True:
            buf = ''
            while True:
                tmpBuf = self.sk.recv(1) # 4 is protobuf string
                if len(tmpBuf) == 0:
                    raise Exception, 'close'
                buf += tmpBuf
                if ord(tmpBuf[0]) & 0x80 == 0:
                    break

            cmd_len = decoder._DecodeVarint32(buf, 0)[0]
            prefix_len = len(buf)
            print "cmd len", cmd_len
            while len(buf) < cmd_len + prefix_len:
                tmpBuf = self.sk.recv(cmd_len + prefix_len - len(buf))
                if len(tmpBuf) == 0:
                    raise Exception, 'close'
                buf += tmpBuf
            cmd = CommandMsg()
            cmd.ParseFromString(buf[prefix_len:])


            if cmd.type == CmdType.FIGHT_RESP:
                print "fight resp"
                self.showFightResp(cmd.content)
            elif cmd.type == CmdType.QUESTION:
                print "question"
                self.showQuestion(cmd.content)
            elif cmd.type == CmdType.FIGHT_RESULT:
                print "fight result"
                self.showResult(cmd.content)
            elif cmd.type == CmdType.FIGHT_REQ:
                print "fight req"
                self.showFightReq(cmd.content)
            elif cmd.type == CmdType.FETCH_PEER_LIST_RESP:
                print "fetch peer list resp"
                self.showFetchList(cmd.content)
            elif cmd.type == CmdType.FIGHT_STATE:
                print "fight state"
                self.showFightState(cmd.content)
            elif cmd.type == CmdType.UNKNOWN_OP:
                print "unkonwn op"
            elif cmd.type == CmdType.FIGHT_CANCEL:
                print "fight cancel"
            else:
                print "bad op"


if __name__ == '__main__':
    u = User()
    usage = '''
    1:connect
    2:fetch list
    3:fight
    4:fight resp
    5:answer
    6:client info
    7:lbs
    8:cancel fight
    9:fetch question
    '''
    p = threading.Thread(target = u.showResp)
    p.start()
    q = threading.Thread(target = u.send_heartbeat)
    q.start()

    while True:
        print usage
        i = raw_input("what do you want to do?")
        if len(i) == 0:
            continue
        i = int(i)
        if i == 1:
            u.connect()
        if i == 2:
            u.sendFetchList()
        if i == 3:
            u.sendFightReq()
        elif i == 4:
            u.sendFightResp()
        elif i == 5:
            u.sendAnswer()
        elif i == 6:
            u.sendClientInfo()
        elif i == 7:
            u.sendLBS()
        elif i == 8:
            u.sendFightCancel()
        elif i == 9:
            u.fetchQuestion()

        


        
        
