#! /usr/local/ruby1.9/bin/ruby
# -*- encoding: utf-8 -*-
require 'kafka'
require 'json'

producer = Kafka::Producer.new({         # ...(1)
  :host => 'localhost',
  :port => 9092,
      :topic =>'topic1',
  :partition => 0
})

time = (Time.now.to_f*1000.0).to_i       # ...(2)
pid = Process.pid

json = Hash.new                          # ...(3)
json['time'] = "#{time}"
json['denpyo-id'] = "P#{pid}"
json['shohinlist'] = []                  # ...(4)
# ↓ここは買い物した商品リスト．．．のつもり
json['shohinlist'].push({:name => "HAKUSAI",  :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'KYABETSU', :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'TAMANEGI', :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'DAIKON',   :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'NINJIN',   :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'JAGAIMO',  :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'BUROKKORI',:count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'NASU',     :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'KABOCHA',  :count=> 1}) if rand > 0.6
json['shohinlist'].push({:name => 'SMILE',    :count=> 1})
                                           if json['shohinlist'].size == 0
# ↑ここまで
message = json.to_json                   # ...(5)
string = "#{message}"
message = Kafka::Message.new(string)     # ...(6)
#message.encode(:compression => Kafka::Message::GZIP_COMPRESSION )
producer.push(message)                   # ...(7)

# この例では１メッセージをBrokerへ投げて終了します
