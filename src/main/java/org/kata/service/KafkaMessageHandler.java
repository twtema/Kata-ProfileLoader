package org.kata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.IndividualDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageHandler {

    private final IndividualService individualService;

    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "${kafka.topic.create}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleMessage(String message) throws JsonProcessingException {
        IndividualDto dto = objectMapper.readValue(message, IndividualDto.class);
        individualService.saveIndividual(dto);
    }
}
