package com.example;

import kafka.api.FetchRequest;
import kafka.api.OffsetRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import kafka.serializer.StringDecoder;

public class ConsumerSample {
    public static void main(String[] args) {
        StringDecoder decoder = new StringDecoder();

        SimpleConsumer consumer = 
             new SimpleConsumer("localhost", 9092, 30*1000, 64*1024); // ... (1)
        long[] offsets = 
            consumer.getOffsetsBefore("sample", 0, 
                                      OffsetRequest.EarliestTime() ,
                                      Integer.MAX_VALUE);             // ... (2)

        for (long offset: offsets) {
            FetchRequest fetchRequest = 
               new FetchRequest("sample", 
                                0,
                                offset,
                                Integer.MAX_VALUE);                   // ... (3)
            ByteBufferMessageSet messages = 
                                 consumer.fetch(fetchRequest);        // ... (4)
            for(MessageAndOffset msg : messages) {
                System.out.println("consumed: [" +
                                   decoder.toEvent(msg.message()) +
                                   "]");                              // ... (5)
            }
        }
        consumer.close();                                             // ... (6)
    }
}
