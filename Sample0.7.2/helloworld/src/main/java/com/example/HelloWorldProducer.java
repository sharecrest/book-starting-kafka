package com.example;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;
public class HelloWorldProducer {

  public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("broker.list", "0:localhost:9092");      // ...(1)
        props.setProperty("producer.type", "sync");
        props.setProperty("compression.codec", "0");
        props.setProperty("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = null;
        try {
            producer = new Producer<String, String>(config);       // ...(2)
            ProducerData<String, String> producerData = 
                new ProducerData<String, String>("topic1",
                                            "Hello World Kafka!"); // ...(3)
            producer.send(producerData);                           // ...(4)
        } finally {
            if (producer != null) producer.close();                // ...(5)
        }
    }
}

