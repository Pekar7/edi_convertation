package com.example.edi_intconvert.cfg.kafka;

import com.example.edi_intconvert.cfg.DevProfileCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@Conditional(DevProfileCondition.class)
public class KafkaTopicConfig {

    @Value("${project.kafka.server}")
    private String kafkaServer;

    @Value("${project.kafka.replication.factor}")
    private Short replicationFactor;

    @Value("${project.kafka.topic.module.hendler.out.xml}")
    private String outQueueXml;

    @Value("${project.kafka.topic.module.hendler.out.edifact}")
    private String outQueueEdifact;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1(){
        return new NewTopic(outQueueXml,1, replicationFactor);
    }

    @Bean
    public NewTopic topic2(){
        return new NewTopic(outQueueEdifact,1, replicationFactor);
    }
}
