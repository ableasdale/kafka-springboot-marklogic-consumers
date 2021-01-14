package com.alexbleasdale.kafkaspringbootmarklogic;

/**
 * Static variables for the general configuration of the Kafka connection
 */
public class KafkaHelper {
    public static final String BOOTSTRAP_HOST = "127.0.0.1:9092";
    public static final String GROUP_CONFIG = "default";
    public static final String XML_TOPIC = "marklogic-xml";
    public static final String JSON_TOPIC = "marklogic"; //-json
}

