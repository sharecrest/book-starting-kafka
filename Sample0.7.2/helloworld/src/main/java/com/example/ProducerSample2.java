package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

public class ProducerSample2{

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        InputStream in = new FileInputStream(
                    new File("ProducerSample.properties"));
//        InputStream in = ProducerSample.class.getClassLoader()
//                .getResourceAsStream("ProducerSample.properties");
        props.load(in);                                                // ... (1)
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer =
                              new Producer<String, String>(config);    // ... (2)
        List<ProducerData<String, String>> list = 
                      new ArrayList<ProducerData<String, String>>();   // ... (3) 
        list.add(new ProducerData<String, String>("English", "Hello."));
        list.add(new ProducerData<String, String>("Deutsch", "Hallo."));
        list.add(new ProducerData<String, String>("English", "Bye."));
        list.add(new ProducerData<String, String>("Deutsch", "Bye."));
        producer.send(list);                                           // ... (4)

        producer.close();                                              // ... (5)
    }
}
