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
            self.send_cmd(CmdType.HEARTBEAT, EmptyMsg())
            time.sleep(30)

    def send_cmd(self, cmd_type, cmd):
        msg = cmd.SerializeToString()
        cmd = CommandMsg()
        cmd.type = cmd_type
        cmd.content = msg
        msg = cmd.SerializeToString()
        self.sk.send(struct.pack("!I%ds" % len(msg), len(msg), msg))
        
    def sendFetchList(self):
        self.send_cmd(CmdType.FETCH_PEER_LIST_REQ, EmptyMsg())

    def sendFightReq(self):
        id = int(raw_input("other's id:"))
        cmd = IFightReq()
        cmd.id = id
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
            cmd_len = struct.unpack("!I", buf[0:4])[0]
            while len(buf) < 4 + cmd_len:
                buf += self.sk.recv(4 + cmd_len - len(buf))
            cmd = CommandMsg()
            cmd.ParseFromString(buf[4:])
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

        


        
        
