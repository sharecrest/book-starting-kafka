package com.example;

import java.util.ArrayList;
import java.util.List;

import kafka.api.FetchRequest;
import kafka.api.OffsetRequest;
import kafka.javaapi.MultiFetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import kafka.serializer.StringDecoder;

public class ConsumerSample3 {
    public static void main(String[] args) {
        StringDecoder decoder = new StringDecoder();

        SimpleConsumer consumer = 
             new SimpleConsumer("localhost", 9092, 30*1000, 64*1024); // ... (1)

        List <FetchRequest> requests = new ArrayList<FetchRequest>();
        long[] offsets =
                consumer.getOffsetsBefore("English",
                                          0, 
                                          OffsetRequest.EarliestTime() , 
                                          Integer.MAX_VALUE);         // ... (2)
        for (long offset: offsets) {
            requests.add(
                      new FetchRequest("English",
                                       0,
                                       offset,
                                       Integer.MAX_VALUE));           // ... (3)
        }
        offsets =
                consumer.getOffsetsBefore("Deutsch", 0, 
                             OffsetRequest.EarliestTime() , Integer.MAX_VALUE);
        for (long offset: offsets) {
            requests.add(
                  new FetchRequest("Deutsch", 0, offset, Integer.MAX_VALUE));
        }

        MultiFetchResponse responses = consumer.multifetch(requests); // ... (4)

        for (ByteBufferMessageSet messages: responses) {
            for(MessageAndOffset msg : messages) {
                System.out.println("consumed: [" + 
                                   decoder.toEvent(msg.message()) +
                                   "]");                              // ... (5)
            }
        }

        consumer.close();                                             // ... (6)
    }
}
