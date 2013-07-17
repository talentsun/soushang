#!/usr/local/bin/python2.7
import MySQLdb

class UserInfo(object):
    def __init__(self):
        self.id = -1
        self.fight_num = 0
        self.win_num = 0

    @classmethod
    def get_user_by_id(cls, id):
        conn = MySQLdb.connect(host='localhost', user='root', passwd='nameLR9969', db='demo_164_2', port=3306, charset='utf8')
        cursor = conn.cursor()
        cursor.execute("select id, fight_num, win_num from fight_user where id = %d" % id)
        row = cursor.fetchone()
        if not row:
            cursor.close()
            return None
        user = UserInfo()
        user.id = id
        user.fight_num = int(row[1])
        user.win_num = int(row[2])
        cursor.close()
        conn.close()
        return user


    def store(self):
        if self.id == -1:
            return

        user = UserInfo.get_user_by_id(self.id)
        conn = MySQLdb.connect(host='localhost', user='root', passwd='nameLR9969', db='demo_164_2', port=3306, charset='utf8')
        cursor = conn.cursor()
        if user:
            cursor.execute("update fight_user set fight_num = %d, win_num = %d where id = %d" % (self.fight_num, self.win_num, self.id))
        else:
            cursor.execute("insert into fight_user (id, fight_num, win_num) values (%d, %d, %d)" % (self.id, self.fight_num, self.win_num))
        cursor.close()
        conn.close()
            

        
