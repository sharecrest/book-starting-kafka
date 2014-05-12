package com.example;

import java.util.Iterator;
import kafka.api.FetchRequest;
import kafka.api.OffsetRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import kafka.serializer.StringDecoder;

public class HelloWorldConsumer {

    public static void main(String[] args) {
        String topic = "topic1";                                        //...(1)
        int partition = 0;  
        int maxNumOffsets = 1;
        SimpleConsumer consumer = 
                   new SimpleConsumer("localhost",9092, 5000, 8192);    //...(2)
        long[] smallest = consumer.getOffsetsBefore(topic,
                                                    partition, 
                                                    OffsetRequest.EarliestTime(),
                                                    maxNumOffsets);     //...(3)
        StringDecoder decoder = new StringDecoder();
        long offset = smallest[0];                                      //...(4)
        while (true) {
            FetchRequest fr = new FetchRequest(topic, partition, offset, 10000);
            ByteBufferMessageSet bbms = consumer.fetch(fr);             //...(5)
            Iterator<MessageAndOffset> ite = bbms.iterator();
            while (ite.hasNext()) {
                MessageAndOffset msg = ite.next();
                System.out.println(decoder.toEvent(msg.message()));
                offset = msg.offset();                                  //...(6)
            }
            try { Thread.sleep(2000); } catch(InterruptedException e){} //...(7)
        }
    }
}

