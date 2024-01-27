package org.kata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.ContactChangeMessageDTO;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageHandler {

    private final IndividualService individualService;
    private final ContactConfirmationService contactConfirmationService;
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "${kafka.topic.create}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleMessage(String message) throws JsonProcessingException {
        IndividualDto dto = objectMapper.readValue(message, IndividualDto.class);
        individualService.saveIndividual(dto);
    }

    /**
     * This method catch kafka message from topic "contact-change-topic" with old & new contacts with confirmation code. Map this message to {@link ContactChangeMessageDTO} and send to {@link ContactConfirmationService}.
     * <p>
     * @param message kafka message with old & new contacts and confirmation code
     * @author Aleksey Mischanchuk
    */
    @KafkaListener(topics = "${kafka.topic.listen}")
    public void handlerContactChangeMessage(String message) throws JsonProcessingException {
        contactConfirmationService.save(objectMapper.readValue(message, ContactChangeMessageDTO.class));
    }

    @KafkaListener(topics = "${kafka.topic.create2}", groupId = "profile-loader")
    public void handleDocumentMessage(String message) throws JsonProcessingException {
        DocumentDto dto = objectMapper.readValue(message, DocumentDto.class);
        documentService.updateDocumentActualState(dto);
    }
}
