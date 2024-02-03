package com.example.edi_intconvert.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSenderService {

    @Value("${project.kafka.topic.module.hendler.out.xml}")
    public String xmlOutQueue;

    @Value("${project.kafka.topic.module.hendler.out.edifact}")
    public String edifactOutQueue;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSenderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendXmlToHendler(String id, String edifact) {
        log.info("Send message with id = {} and document = {} ", id, edifact);
        kafkaTemplate.send(xmlOutQueue, edifact);
    }

    public void sendEdifactToHendler(String id, String edifact) {
        log.info("Send message with id = {} and document = {} ", id, edifact);
        kafkaTemplate.send(edifactOutQueue, edifact);
    }

}
