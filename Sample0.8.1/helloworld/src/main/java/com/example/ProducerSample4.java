package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProducerSample4 {

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        InputStream in = new FileInputStream(
                               new File("ProducerSample3.properties"));
//        InputStream in = ProducerSample4.class.getClassLoader()
//                .getResourceAsStream("ProducerSample3.properties");
        props.load(in);                                                // ... (1)
        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer =
                        new Producer<String, String>(config);          // ... (2)
        List<KeyedMessage<String, String>> list =
                new ArrayList<KeyedMessage<String, String>>();         // ... (3)
        list.add(new KeyedMessage<String, String>("English", "Hello."));
        list.add(new KeyedMessage<String, String>("Deutsch", "Hallo."));
        list.add(new KeyedMessage<String, String>("English", "Bye."));
        list.add(new KeyedMessage<String, String>("Deutsch", "Bye."));
        producer.send(list);                                           // ... (4)
        producer.close();                                              // ... (5)
    }
}
