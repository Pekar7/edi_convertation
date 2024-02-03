package com.example.edi_intconvert.service.kafka;


import com.example.edi_intconvert.dto.MessageDtoKfk;
import com.example.edi_intconvert.service.convert.EdifactConvertService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class KafkaListenerService implements ConsumerSeekAware {

    private final EdifactConvertService edifactConvertService;

    @Autowired
    public KafkaListenerService(EdifactConvertService edifactConvertService) {
        this.edifactConvertService = edifactConvertService;
    }

    @Transactional(transactionManager = "tmKafkaMessage",
            rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    @KafkaListener(groupId = "${project.kafka.consumer.group.id}", clientIdPrefix = "EDIFACTsubscriber",
            topics={"${project.kafka.topic.module.convert.in.edifact}"}, containerFactory = "batchFactoryInt")
    public void edifactMsgListener(List<ConsumerRecord<String, String>> messages){
        try {
            for (ConsumerRecord<String, String> message : messages) {
                edifactConvertService.convertDocumentToJson(getMessage(message));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static MessageDtoKfk getMessage(ConsumerRecord<String, String> consumerRecords) {
        MessageDtoKfk msg = new MessageDtoKfk();
        msg.setId(consumerRecords.key());
        msg.setData(consumerRecords.value());
        return msg;
    }
}