package com.example;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

public class HelloWorldConsumer {

    public static void main(String[] args) {
        System.out.println("Consumer START．");

        String clientName = "HelloWorldConsumer";
        String topic = "topic1";                                        //...(1)
        int partition = 0;
        int maxNumOffsets = 5;

        SimpleConsumer consumer =
          new SimpleConsumer("localhost",9092, 5000, 8192, clientName); //...(2)

        TopicAndPartition topicAndPartition =
                               new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestOffsetInfo =
                           new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestOffsetInfo.put(
              topicAndPartition,
              new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1));
        OffsetRequest offsetReq = new OffsetRequest(
                                         requestOffsetInfo,
                                         kafka.api.OffsetRequest.CurrentVersion(),
                                         clientName);
        OffsetResponse response = consumer.getOffsetsBefore(offsetReq); //...(3)
        long[] smallest = response.offsets(topic, partition);

        long offset = smallest[0];                                      //...(4)
        while (true) {
            System.out.println("Testing single fetch offset="+offset);
            FetchRequest fr = new FetchRequestBuilder()
                                     .clientId(clientName)
                                     .addFetch(topic, 0, offset, 10000)
                                     .build();
            FetchResponse fetchResponse = consumer.fetch(fr);          //...(5)
            Iterator<MessageAndOffset> ite =
                          fetchResponse.messageSet(topic, partition).iterator();
            while (ite.hasNext()) {
                MessageAndOffset messageAndOffset = ite.next();
                ByteBuffer payload = messageAndOffset.message().payload();
                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                try {
                    System.out.println(new String(bytes, "UTF-8"));
                } catch (Exception e) {}
                offset = messageAndOffset.nextOffset();                 //...(6)
            }
            try { Thread.sleep(2000); } catch (InterruptedException e){}//...(7)
        }
    }
}

