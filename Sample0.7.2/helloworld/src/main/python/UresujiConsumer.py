#! /usr/local/Python-2.7.5/bin/python
# coding:utf-8
from kazoo.client import KazooClient
from samsa.cluster import Cluster
import json
import time
import redis
zookeeper = KazooClient(hosts='localhost:2181')  #...(1)
zookeeper.start()                                #...(2)
cluster = Cluster(zookeeper)                     #...(3)
topic = cluster.topics['topic1']                 #...(4)
consumer = topic.subscribe('group-name1')        #...(5)
r = redis.Redis(host='localhost',port=6379,db=0) #...(6)
for message in consumer:                         #...(7)
    jmes=json.loads(message)                     #...(8)
    shohinlist = jmes['shohinlist']
    for shohin in shohinlist:                    #...(9)
        name = shohin['name']
        count = shohin['count']
        r.zincrby('ranking', name, count)        #...(10)
