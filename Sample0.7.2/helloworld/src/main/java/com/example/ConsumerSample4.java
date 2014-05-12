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
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

public class ConsumerSample4 {
    public static void main(String[] args) throws IOException, 
                                                InterruptedException {

        List<Thread> threads = new ArrayList<Thread>();

        Properties props = new Properties();
        InputStream in = new FileInputStream(
                           new File("ConsumerSample4.properties"));   // ... (1)
//        InputStream in = ProducerSample.class.getClassLoader()
//                .getResourceAsStream("ConsumerSample4.properties");
        props.load(in);
        ConsumerConfig config =   new ConsumerConfig(props);
        ConsumerConnector connector = 
                    Consumer.createJavaConsumerConnector(config);     // ... (2)

        Map<String, Integer> topicCountMap = 
                                new HashMap<String, Integer>();       // ... (3)
        topicCountMap.put("English", 3);
        topicCountMap.put("Deutsch", 3);

        Map<String, List<KafkaStream<Message>>> messageStreamMap = 
                connector.createMessageStreams(topicCountMap);        // ... (4)
        for (Entry<String, List<KafkaStream<Message>>> entry : 
                                     messageStreamMap.entrySet()) {
            List<KafkaStream<Message>> streams = entry.getValue();
            for(KafkaStream<Message> stream: streams) {
                final ConsumerIterator<Message> messages = 
                                                 stream.iterator();
                Thread thread = new Thread(                           // ... (5)
                    new Runnable() {
                        @Override
                        public void run() {
                            StringDecoder decoder = new StringDecoder();
                            while(messages.hasNext()) {               // ... (6)
                                MessageAndMetadata<Message> 
                                     messageAndMetaData = messages.next();
                                System.out.println("consumed: " +
                                        "topic=["  + 
                                        messageAndMetaData.topic() + "] " + 
                                        "message=[" + 
                                        decoder.toEvent(
                                            messageAndMetaData.message()) + "]");
                            }
                        }
                    });
                thread.start();
                threads.add(thread);
            }
        }        

        for (Thread thread: threads) {                                // ... (7)
            thread.join();
        }

        connector.shutdown();                                         // ... (8)
    }

}
