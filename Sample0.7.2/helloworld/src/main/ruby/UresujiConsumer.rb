#! /usr/local/ruby1.9/bin/ruby
# -*- encoding: utf-8 -*-
require 'kafka'
require 'json'
require 'redis'

consumer = Kafka::Consumer.new({            # ...(1)
  :host => 'localhost',
  :port => 9092,
  :topic =>'topic1',
  :partition => 0,
  :offset => 0
})

redis = Redis.new({                         # ...(2)
  :host => 'localhost',
  :port => 6379
})
redis.ping

p "Consumer started."
consumer.loop do |messages|                 # ...(3)
  messages.each do |message|                # ...(4)
    jmes = JSON.parse(message.payload)      # ...(5)
#    puts message.payload
    time = jmes['time']
    denpyoId = jmes['denpyo-id']
    shohinlist = jmes['shohinlist']
    shohinlist.each do |shohin|             # ...(6)
      name = shohin['name']
      count = shohin['count']
      redis.zincrby "ranking", count, name  # ...(7)
    end
  end
end
