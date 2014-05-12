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
   
public class ProducerSample {  

    public static void main(String[] args) throws IOException {
    
        Properties props = new Properties();
        InputStream in = new FileInputStream(
                               new File("ProducerSample.properties")); 
//        InputStream in = ProducerSample.class.getClassLoader()
//                .getResourceAsStream("ProducerSample.properties");
        props.load(in);                                                // ... (1)
        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = 
                        new Producer<String, String>(config);          // ... (2)
        ProducerData<String, String> data = 
                 new ProducerData<String, String>("sample", "Hello."); // ... (3)
        producer.send(data);                                           // ... (4)
        producer.close();                                              // ... (5)
    }
}
