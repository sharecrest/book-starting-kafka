#! /usr/local/Python-2.7.5/bin/python
# coding:utf-8
from kazoo.client import KazooClient
from samsa.cluster import Cluster
from random import random
import time
import os
import json
epochmilli = int(round(time.time() * 1000))
pid = os.getpid()
i = 1
zookeeper = KazooClient(hosts='localhost:2181') #...(1)
zookeeper.start()                       
cluster = Cluster(zookeeper)                    #...(2)
topic = cluster.topics['topic1']                #...(3)
msghash = {}                                    #...(4)
msghash['time'] = epochmilli                    #...(5)
msghash['denpyo-id'] = "P%(pid)d-%(i)d" % locals()
msghash['shohinlist']=[]
# ↓ここは買い物した商品リスト．．．のつもり           #...(6)
if random() > 0.6 : msghash['shohinlist'].append({'name': "HAKUSAI",  'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "KYABETSU", 'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "TAMANEGI", 'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "DAIKON",   'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "NINJIN",   'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "JAGAIMO",  'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "BUROKKORI",'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "NASU",     'count': 1})
if random() > 0.6 : msghash['shohinlist'].append({'name': "KABOCHA",  'count': 1})
if len(msghash['shohinlist']) == 0 : msghash['shohinlist']
                                           .append({'name': "SMILE",  'count': 1})
# ↑ここまで
message = json.dumps(msghash)                   #...(7)
topic.publish(message)                          #...(8)
