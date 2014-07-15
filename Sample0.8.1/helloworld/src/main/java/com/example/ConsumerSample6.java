package com.example;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.message.MessageAndOffset;

public class ConsumerSample6 {
    public static void main(String[] args) {
        String topic="sample";
        int partition=0;
        SimpleConsumer consumer = new SimpleConsumer(
             "localhost", 9092, 30*1000, 64*1024, "ConsumerSample6"); // ... (1)
        TopicAndPartition topicAndPartition =
                new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestOffsetInfo =
            new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();

        requestOffsetInfo.put(
            topicAndPartition,
            new PartitionOffsetRequestInfo(
                      kafka.api.OffsetRequest.EarliestTime(), 10000));
        OffsetRequest offsetReq = new OffsetRequest(
                requestOffsetInfo,
                kafka.api.OffsetRequest.CurrentVersion(),
                "ConsumerSample6");
        OffsetResponse response =  consumer.getOffsetsBefore(offsetReq);
        long[] offsets = response.offsets(topic, partition);          // ... (2)

        for (long offset: offsets) {
            FetchRequest fetchRequest = new FetchRequestBuilder()
                .clientId("ConsumerSample6")
                .addFetch(topic, partition, offset, 10000000)
                .build();                                             // ... (3)

            FetchResponse fetchResponse=consumer.fetch(fetchRequest); // ... (4)
            Iterator<MessageAndOffset> ite =
                    fetchResponse.messageSet(topic, partition).iterator();
            while (ite.hasNext()) {
                MessageAndOffset messageAndOffset = ite.next();
                ByteBuffer payload = messageAndOffset.message().payload();
                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                try {
                    System.out.println(new String(bytes, "UTF-8"));   // ... (5)
                } catch (Exception e) {}
            }
        }
        consumer.close();
    }
}
