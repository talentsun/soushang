#!/usr/local/bin/python
#coding=utf8

from utils import *
import random
from trie import Trie, TrieNode


logger = InitLogger("client_manager", logging.DEBUG, "../log/client_manager.log")

class TestClient(object):
    def __init__(self):
        self.longitude = 0
        self.latitude = 0

class LBSClientManager(object):
    def __init__(self):
        self.trie = Trie(4, ord('0'))
        self.max_len = 20
        self.clients = {}

    def show_all_clients(self):
        print "show all clients"
        objs = self.trie.root.get_offsprings()
        for o in objs:
            print o.longitude, o.latitude

    def get(self, id):
        return self.clients.get(id)

    def get_prefix(self, client):
        lon = client.longitude
        lat = client.latitude
        if lon < -180:
            lon = -180.0
        if lon > 180:
            lon = 180.0
        if lat > 90:
            lat = 90.0
        if lat < -90:
            lat = -90.0

        lon_min = -180.0
        lon_max = 180.0
        lat_min = -90.0
        lat_max = 90.0
        ret = ''
        for i in range(self.max_len):
            cur = 0
            lon_mid = (lon_min + lon_max) / 2
            lat_mid = (lat_min + lat_max) / 2
            #print lon_min, lon_mid, lon_max
            #print lat_min, lat_mid, lat_max
            if lat < lat_mid:
                cur += 2
                lat_max = lat_mid
            else:
                lat_min = lat_mid
            if lon > lon_mid:
                cur += 1
                lon_min = lon_mid
            else:
                lon_max = lon_mid
            ret += str(cur)

        return ret

    def add_client(self, client):
        if client.id in self.clients:
            logger.error("id %d already in" % client.id)
            return
        logger.debug("add client longitude %f latitude %f id %d" % (client.longitude, client.latitude, client.id))
        prefix = self.get_prefix(client)
        logger.debug("client %d prefix %s" % (client.id, prefix))
        self.trie.add_one(prefix, client)
        self.clients[client.id] = client
        

    def remove_client(self, client):
        if client.id not in self.clients:
            logger.error("id %d not in")
            return

        prefix = self.get_prefix(client)
        logger.debug("remove client %d longitude %f latitude %f prefix %s" % (client.id, client.longitude, client.latitude, prefix))
        self.trie.remove_one(prefix, client)
        del self.clients[client.id]

    def get_left(self, prefix_num, len):
        if len <= 0:
            return -1
        if prefix_num == -1:
            return -1
        m = prefix_num % 4
        if m == 1 or m == 3:
            return prefix_num - 1

        up = self.get_left(prefix_num >> 2, len - 1)
        if up < 0:
            return -1
        cur = up << 2
        return cur + m + 1

    def get_right(self, prefix_num, len):
        if len <= 0:
            return -1
        if prefix_num == -1:
            return -1
        m = prefix_num % 4
        if m == 0 or m == 2:
            return prefix_num + 1
        up = self.get_right(prefix_num >> 2, len - 1)
        if up < 0:
            return -1
        cur = up << 2
        return cur + m - 1

    def get_up(self, prefix_num, len):
        if len <= 0:
            return -1
        if prefix_num == -1:
            return -1
        m = prefix_num % 4
        if m == 2 or m == 3:
            return prefix_num - 2
        up = self.get_up(prefix_num >> 2, len - 1)
        if up < 0:
            return -1
        cur = up << 2
        return cur + m + 2

    def get_down(self, prefix_num, len):
        if len <= 0:
            return -1
        if prefix_num == -1:
            return -1
        m = prefix_num % 4
        if m == 0 or m == 1:
            return prefix_num + 2
        up = self.get_down(prefix_num >> 2, len - 1)
        if up < 0:
            return -1
        cur = up << 2
        return cur + m - 2

    def get_down_left(self, prefix_num, len):
        if len <= 0:
            return -1
        return self.get_down(self.get_left(prefix_num, len), len)

    def get_down_right(self, prefix_num, len):
        if len <= 0:
            return -1
        return self.get_down(self.get_right(prefix_num, len), len)

    def get_up_left(self, prefix_num, len):
        if len <= 0:
            return -1
        return self.get_up(self.get_left(prefix_num, len), len)

    def get_up_right(self, prefix_num, len):
        if len <= 0:
            return -1
        return self.get_up(self.get_right(prefix_num, len), len)

    def get_prefix_str(self, prefix_num, max_len):
        prefix = ''
        if prefix_num < 0:
            return ''
        while prefix_num > 3:
            prefix += chr(prefix_num % 4 + ord('0'))
            prefix_num = prefix_num >> 2
        prefix += chr(prefix_num % 4 + ord('0'))

        prefix = prefix[-1:-len(prefix) - 1:-1]
        if len(prefix) < max_len:
            prefix = '0' * (max_len - len(prefix)) + prefix

        return prefix

    def get_near_prefix(self, prefix):
        logger.debug("get near prefix for %s" % prefix)
        prefix_num = int(prefix, 4)
        near_prefixs = []
        nears = (
            self.get_up_left(prefix_num, len(prefix)),
            self.get_up(prefix_num, len(prefix)),
            self.get_up_right(prefix_num, len(prefix)),
            self.get_left(prefix_num, len(prefix)),
            prefix_num,
            self.get_right(prefix_num, len(prefix)),
            self.get_down_left(prefix_num, len(prefix)),
            self.get_down(prefix_num, len(prefix)),
            self.get_down_right(prefix_num, len(prefix)))

        for i in range(len(nears)):
            if nears[i] == -1:
                near_prefixs.append('')
            else:
                near_prefixs.append(self.get_prefix_str(nears[i], len(prefix)))

        return near_prefixs


    def get_near(self, client, num):
        logger.debug("get near for %d num %d" % (client.id, num))
        res = []
        prefix = self.get_prefix(client)
        while len(prefix) > 0:
            nears = self.get_near_prefix(prefix)
            cur_num = 0
            son_nodes = []
            for s in nears:
                if len(s) > 0:
                    trie_node = self.trie.get_one(s)
                    if trie_node:
                        cur_num += trie_node.offspring_num
                        son_nodes.append(trie_node)

            if cur_num >= num + 1 or len(prefix) == 1:
                startIndex = random.randint(0, len(son_nodes) - 1)
                for i in range(len(son_nodes)):
                    s = (startIndex + i) % len(son_nodes)
                    trie_node = son_nodes[s]
                    offsprings = trie_node.get_offsprings()
                    for obj in offsprings:
                        if obj != client:
                            res.append(obj)
                            if len(res) >= num:
                                return res
            prefix = prefix[:-1]


        logger.debug("get near res %d" % len(res))
        return res



if __name__ == '__main__':
    #import pdb
    #pdb.set_trace()
    cm = LBSClientManager()
    client = TestClient()
    client.longitude = -150
    client.latitude = -45
    cm.add_client(client)

    client = TestClient()
    client.longitude = -151
    client.latitude = -46
    cm.add_client(client)

    client = TestClient()
    client.longitude = -15
    client.latitude = -4
    cm.add_client(client)

    client = TestClient()
    client.longitude = -150
    client.latitude = -45.1
    cm.add_client(client)

    cm.show_all_clients()
    prefix = cm.get_prefix(client)

    print "find nears for %f %f" % (client.longitude, client.latitude)
    nears = cm.get_near(client ,2)
    for o in nears:
        print o.longitude, o.latitude
    prefix_num = int(prefix, 4)
    print prefix
    #print int(prefix, 4)
    #print cm.get_prefix_str(prefix_num)
    #print cm.get_prefix_str(cm.get_up(prefix_num))
    print cm.get_near_prefix(prefix)
    #print cm.get_prefix_str(cm.get_up_right(prefix_num, cm.max_len))
        
        
