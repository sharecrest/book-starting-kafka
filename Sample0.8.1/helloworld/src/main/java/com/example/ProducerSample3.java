package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProducerSample3 {

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        InputStream in = new FileInputStream(
                               new File("ProducerSample3.properties"));
//        InputStream in = ProducerSample3.class.getClassLoader()
//                .getResourceAsStream("ProducerSample3.properties");
        props.load(in);                                                // ... (1)
        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer =
                        new Producer<String, String>(config);          // ... (2)
        KeyedMessage<String, String> data =
                new KeyedMessage<String, String>("sample", "Hello.");  // ... (3)
        producer.send(data);                                           // ... (4)
        producer.close();                                              // ... (5)
    }
}
