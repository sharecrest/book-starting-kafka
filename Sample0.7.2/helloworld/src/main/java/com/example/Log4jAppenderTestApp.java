package com.example;
import org.apache.log4j.Logger;
public class Log4jAppenderTestApp {
    static protected Logger log = Logger.getLogger( Log4jAppenderTestApp.class );
    public static void main(String[] args) {
        log.info("Kafka Log4jAppender!");
    }
}
