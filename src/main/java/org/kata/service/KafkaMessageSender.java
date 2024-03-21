package org.kata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.kata.utils.Constants.ConstantsKafka.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(IndividualDto dto) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KAFKA_TOPIC_CREATE, MESSAGE_ID, dto);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable ex) {
                log.error(LOG_MESSAGE_SEND_ERROR, KAFKA_TOPIC_CREATE, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info(LOG_MESSAGE_SEND_SUCCESS, KAFKA_TOPIC_CREATE);
            }
        });
    }
}
