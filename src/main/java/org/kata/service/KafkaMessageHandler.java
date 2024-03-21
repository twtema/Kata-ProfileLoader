package org.kata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.ContactChangeMessageDTO;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.kata.utils.Constants.ConstantsKafka.*;

@Service
@RequiredArgsConstructor
public class KafkaMessageHandler {

    private final IndividualService individualService;
    private final ContactConfirmationService contactConfirmationService;
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KAFKA_TOPIC_CREATE, groupId = KAFKA_CONSUMER_GROUP_ID,
            containerFactory = FILTER_KAFKA_LISTENER_CONTAINER_FACTORY)
    public void handleMessage(String message) throws JsonProcessingException {
        IndividualDto dto = objectMapper.readValue(message, IndividualDto.class);
        individualService.saveIndividual(dto);
    }

    /**
     * This method catch kafka message from topic "contact-change-topic" with old & new contacts with confirmation code. Map this message to {@link ContactChangeMessageDTO} and send to {@link ContactConfirmationService}.
     * <p>
     *
     * @param message kafka message with old & new contacts and confirmation code
     * @author Aleksey Mischanchuk
     */
    @KafkaListener(topics = KAFKA_TOPIC_LISTEN)
    public void handlerContactChangeMessage(String message) throws JsonProcessingException {
        contactConfirmationService.save(objectMapper.readValue(message, ContactChangeMessageDTO.class));
    }

    @KafkaListener(topics = KAFKA_TOPIC_CREATE_2, groupId = PROFILE_LOADER_GROUP_ID)
    public void handleDocumentMessage(String message) throws JsonProcessingException {
        DocumentDto dto = objectMapper.readValue(message, DocumentDto.class);
        documentService.updateDocumentActualState(dto);
    }
}
