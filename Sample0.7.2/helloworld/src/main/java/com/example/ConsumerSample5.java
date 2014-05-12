package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

public class ConsumerSample5 {
    public static void main(String[] args) throws IOException,
                                                  InterruptedException {

        List<Thread> threads = new ArrayList<Thread>();

        Properties props = new Properties();
        InputStream in = new FileInputStream(
                          new File("ConsumerSample5.properties"));    // ... (1)
//        InputStream in = ProducerSample.class.getClassLoader()
//               .getResourceAsStream("ConsumerSample5.properties");
        props.load(in);
        ConsumerConfig config =   new ConsumerConfig(props);
        ConsumerConnector connector = 
                      Consumer.createJavaConsumerConnector(config);   // ... (2)

        TopicFilter filter = new Whitelist("English|Deutsch");        // ... (3)

        List<KafkaStream<Message>> streams = 
                connector.createMessageStreamsByFilter(filter);       // ... (4)
        for(KafkaStream<Message> stream: streams) {
            final ConsumerIterator<Message> messages = stream.iterator();
            Thread thread = new Thread(                               // ... (5)
                new Runnable() {
                    @Override
                    public void run() {
                        StringDecoder decoder = new StringDecoder();
                        while(messages.hasNext()) {                   // ... (6)
                            MessageAndMetadata<Message> messageAndMetaData = 
                                                                messages.next();
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

        for (Thread thread: threads) {                                // ... (7)
            thread.join();
        }

        connector.shutdown();                                         // ... (8)
    }

}
