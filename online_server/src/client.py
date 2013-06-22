#!/usr/local/bin/python
#coding:utf-8
from message_pb2 import *
from cmd_type import CmdType
from socket import socket
import struct
import time
import threading


class User(object):
    def __init__(self):
        self.sk = None
        pass

    def connect(self):
        if self.sk:
            return
        self.sk = socket()
        self.sk.connect( ('127.0.0.1', 9300) )

    def send_heartbeat(self):
        while not self.sk:
            time.sleep(1)


        while True:
            self.sk.send(struct.pack("!HH", CmdType.HEARTBEAT, 0))
            time.sleep(30)

    def send_cmd(self, cmd_type, cmd):
        req = cmd.SerializeToString()
        req = struct.pack("!HH%ds" % len(req), cmd_type, len(req), req)
        self.sk.send(req)
        
    def sendFetchList(self):
        self.sk.send(struct.pack("!HH", CmdType.FETCH_PEER_LIST_REQ, 0))

    def sendFightReq(self):
        id = int(raw_input("other's id:"))
        cmd = IFightReq()
        cmd.id = id
        self.send_cmd(CmdType.FIGHT_REQ, cmd)

    def sendFightResp(self):
        print "0 show agree others disgree"
        id = int(raw_input(""))
        cmd = IFightResp()
        cmd.result = id
        self.send_cmd(CmdType.FIGHT_RESP, cmd)


    def sendAnswer(self):
        i = int(raw_input("your choices is:"))
        index = int(raw_input("question index is:"))
        cmd = IAnswer()
        cmd.index = index
        cmd.choose = i
        self.send_cmd(CmdType.ANSWER, cmd)


    def showFetchList(self, buf):
        cmd = OPeerListResp()
        cmd.ParseFromString(buf)
        for i in cmd.users:
            print "one user", i.id

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
        print "question:", cmd.statement
        print "index:", cmd.index
        print "all:", cmd.all
        for op in cmd.options:
            print op

    def showResult(self, buf):
        cmd = OFightResult()
        cmd.ParseFromString(buf)
        print "1 is win 2 is lose, your result is", cmd.result

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
            while len(buf) < 4:
                buf += self.sk.recv(4 - len(buf))
            cmd_type, cmd_len = struct.unpack("!HH", buf[0:4])
            while len(buf) < 4 + cmd_len:
                buf += self.sk.recv(4 + cmd_len - len(buf))
            if cmd_type == CmdType.FIGHT_RESP:
                print "fight resp"
                self.showFightResp(buf[4:])
            elif cmd_type == CmdType.QUESTION:
                print "question"
                self.showQuestion(buf[4:])
            elif cmd_type == CmdType.FIGHT_RESULT:
                print "fight result"
                self.showResult(buf[4:])
            elif cmd_type == CmdType.FIGHT_REQ:
                print "fight req"
                self.showFightReq(buf[4:])
            elif cmd_type == CmdType.FETCH_PEER_LIST_RESP:
                print "fetch peer list resp"
                self.showFetchList(buf[4:])
            elif cmd_type == CmdType.FIGHT_STATE:
                print "fight state"
                self.showFightState(buf[4:])


if __name__ == '__main__':
    u = User()
    usage = '''
    1:connect
    2:fetch list
    3:fight
    4:fight resp
    5:answer'''
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

        


        
        
