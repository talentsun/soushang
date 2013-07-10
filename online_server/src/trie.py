#!/usr/local/bin/python
#coding:utf-8
from sets import Set

class TrieNode(object):
    def __init__(self, son_num, base_chr):
        self.sons = []
        for i in range(son_num):
            self.sons.append(None)
        self.parent = None
        self.offspring_num = 0 #include self
        self.obj_set = Set()
        self.base_chr = base_chr

    def get_offsprings(self):
        res = []
        for i in self.obj_set:
            res.append(i)
        for o in self.sons:
            if o:
                res.extend(o.get_offsprings())

        return res

    def add_son(self, prefix, obj):
        ret = 0
        if prefix == '':
            if obj not in self.obj_set:
                self.obj_set.add(obj)
                ret = 1
        else:
            cur_chr = ord(prefix[0])
            index = cur_chr - self.base_chr
            if self.sons[index] == None:
                self.sons[index] = TrieNode(len(self.sons), self.base_chr)
            ret = self.sons[index].add_son(prefix[1:], obj)

        self.offspring_num += ret
        return ret

    def get_son(self, prefix):
        if prefix == '':
            return self
        cur_chr = ord(prefix[0])
        index = cur_chr - self.base_chr
        if self.sons[index] == None:
            return None
        return self.sons[index].get_son(prefix[1:])

    def remove_son(self, prefix, obj):
        ret = 0
        if prefix == '':
            if obj in self.obj_set:
                ret = 1
                self.obj_set.remove(obj)
        else:
            cur_chr = ord(prefix[0])
            index = cur_chr - self.base_chr
            if self.sons[index] != None:
                ret = self.sons[index].remove_son(prefix[1:], obj)

        self.offspring_num -= ret

        return ret
        
        
class Trie(object):
    def __init__(self, son_num, base_chr):
        self.son_num = son_num
        self.root = TrieNode(son_num, base_chr)

    def add_one(self, prefix, obj):
        self.root.add_son(prefix, obj)

    def get_one(self, prefix):
        return self.root.get_son(prefix)

    def remove_one(self, prefix, obj):
        return self.root.remove_son(prefix, obj)

