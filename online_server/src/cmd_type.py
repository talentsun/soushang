#coding:utf8
class CmdType(object):
    FETCH_PEER_LIST_REQ = 1
    FETCH_PEER_LIST_RESP = 2

    FIGHT_REQ = 3
    FIGHT_RESP = 4
    
    QUESTION = 5
    ANSWER = 6
    FIGHT_STATE = 7
    FIGHT_RESULT = 8

    CLIENT_INFO = 9

    CLIENT_LBS = 11

    FIGHT_CANCEL = 12
    FIGHT_QUIT = 13

    ON_LINE = 14
    OFF_LINE = 15

    LOGIN_SUCC = 16
    LOGIN_FAIL = 17

    UNKNOWN_OP = 1000
    HEARTBEAT = 1001


