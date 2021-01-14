package com.alexbleasdale.kafkaspringbootmarklogic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marklogic.client.extra.gson.GSONHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class KafkaMarkLogicConsumerService {

    private final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Gson gson;

    @KafkaListener(topics = KafkaHelper.XML_TOPIC)
    public void getRawAtomXmlFromKafka(String string) {
        LOG.info(String.format("XML Listener received payload from topic %s", KafkaHelper.XML_TOPIC));
        //LOG.info(String.format("Full String from HTTP POST: %s", string));
           /*
         Second test: can I write this string (as an XML document) to MarkLogic (as a simple test)
         from inside the Kafka Consumer?
         I'm using the MarkLogic Java Client API to handle the string data
         */
        DocumentMetadataHandle metadataHandle =
                 new DocumentMetadataHandle();
        metadataHandle.getCollections().addAll("atom-emitter", KafkaHelper.XML_TOPIC);
        MarkLogicClientProvider.writeXmlDocument(String.format("/%s.xml", java.util.UUID.randomUUID()), metadataHandle, string);
    }

    @KafkaListener(topics = KafkaHelper.JSON_TOPIC)
    public void getJSONFromKafka(String string) {
        LOG.info(String.format("JSON Listener received payload from topic %s", KafkaHelper.JSON_TOPIC));
        GSONHandle gsonHandle = new GSONHandle(JsonParser.parseString(string));
        gsonHandle.setMimetype("application/json");

        JsonNode actualObj = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
           actualObj = mapper.readTree(string);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JacksonHandle jh = new JacksonHandle();
        jh.set(actualObj);
        LOG.info("Fmt: "+jh.getFormat());
        DocumentMetadataHandle metadataHandle =
                new DocumentMetadataHandle();
        metadataHandle.getCollections().addAll("atom-emitter", KafkaHelper.JSON_TOPIC);
        MarkLogicClientProvider.writeJsonDocument(String.format("/%s.json", java.util.UUID.randomUUID()), metadataHandle, jh);
    }

}
