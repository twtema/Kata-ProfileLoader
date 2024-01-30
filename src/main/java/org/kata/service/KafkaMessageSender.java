package org.kata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.create}")
    private String kafkaTopic;

    @Value("${messageId}")
    private String messageId;

    public void sendMessage(IndividualDto dto) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaTopic, messageId, dto);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message to topic:{}, \n{}", kafkaTopic, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Message send to topic:{}", kafkaTopic);
            }
        });
    }
}
