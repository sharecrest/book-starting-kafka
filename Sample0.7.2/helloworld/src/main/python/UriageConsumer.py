#! /usr/local/Python-2.7.5/bin/python
# coding:utf-8
from kazoo.client import KazooClient
from samsa.cluster import Cluster
import json
import time
import redis

zookeeper = KazooClient(hosts='localhost:2181')            #...(1)
zookeeper.start()
cluster = Cluster(zookeeper)                               #...(2)
topic = cluster.topics['topic1']                           #...(3)
r = redis.Redis(host='localhost',port=6379,db=0)           #...(4)
r.delete('uriage')

t_from=(2013, 7, 21, 13, 0, 0, 0, 0, -1)                   #...(5)
t_to=(2013, 7, 21, 14, 0, 0, 0, 0, -1)
time_from_millis=int(round(time.mktime(t_from) * 1000))
time_to_millis=int(round(time.mktime(t_to) * 1000))

for partition in topic.partitions:                         #...(6)
    pnum_from=partition.broker.client.offsets(
                partition.topic.name, 
                partition.number, time_from_millis, 1)
    pnum_to=partition.broker.client.offsets(
                partition.topic.name, 
                partition.number, time_to_millis, 1)

    offset = pnum_from[0]
    while 1:
        for mesobj in partition.fetch(offset, 8192):       #...(7)
            if mesobj != None:
                offset = mesobj.next_offset
                jmes=json.loads(str(mesobj))
                mestime = jmes['time']
                if mestime < time_from_millis: continue    #...(8)
                if time_to_millis < mestime:               #...(9)
                    offset = -1
                    break
                shohinlist = jmes['shohinlist']            #...(10)
                for shohin in shohinlist:
                    name = shohin['name']
                    count = shohin['count']
                    r.zincrby('uriage', name, count)       #...(11)
            else:
                offset = -1
                break
        if offset < 0: break
