package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

public class ConsumerSample7 {
    public static void main(String[] args) throws IOException,
                                                  InterruptedException {

        List<Thread> threads = new ArrayList<Thread>();

        Properties props = new Properties();
        InputStream in = new FileInputStream(
                          new File("ConsumerSample7.properties"));  // ... (1)
//        InputStream in = ConsumerSample7.class.getClassLoader()
//                .getResourceAsStream("ConsumerSample7.properties");
        props.load(in);
        final ConsumerConfig config = new ConsumerConfig(props);
        ConsumerConnector connector =
                      Consumer.createJavaConsumerConnector(config);   // ... (2)

        Map<String, Integer> topicCountMap =
                                     new HashMap<String, Integer>();  // ... (3)
        topicCountMap.put("English", 3);
        topicCountMap.put("Deutsch", 3);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreamMap =
                       connector.createMessageStreams(topicCountMap); // ... (4)
        for (Entry<String, List<KafkaStream<byte[], byte[]>>> entry :
                                                 messageStreamMap.entrySet()) {
                List<KafkaStream<byte[], byte[]>> streams = entry.getValue();
            for(KafkaStream<byte[], byte[]> stream: streams) {
                final ConsumerIterator<byte[], byte[]> messages = stream.iterator();
                Thread thread = new Thread(                               // ... (5)
                    new Runnable() {
                        @Override
                        public void run() {
                            StringDecoder decoder = new StringDecoder(config.props());
                            while(messages.hasNext()) {                   // ... (6)
                                MessageAndMetadata<byte[], byte[]>
                                     messageAndMetaData = messages.next();
                                System.out.println("consumed: " +
                                        "topic=["  +
                                        messageAndMetaData.topic() + "] " +
                                        "message=[" +
                                        decoder.fromBytes(
                                            messageAndMetaData.message()) + "]");
                            }
                        }
                    });
                thread.start();
                threads.add(thread);
            }
        }

        for (Thread thread: threads) {                                    // ... (7)
            thread.join();
        }

        connector.shutdown();                                             // ... (8)
    }

}
