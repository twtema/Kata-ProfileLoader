package org.kata.service;

import org.assertj.core.api.Assertions;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.util.FileUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.kata.util.MapperUtil.deserializeIndividualDto;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class KafkaMessageSenderTest {
    private String kafkaTopic;
    private String messageId;
    private IndividualDto testIndividualDto;
    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    KafkaMessageSender kafkaMessageSender;

    @Before
    public void setUp() throws IOException {
        ReflectionTestUtils.setField(kafkaMessageSender, "kafkaTopic", "createIndividual");
        ReflectionTestUtils.setField(kafkaMessageSender, "messageId", "ProfileLoader");
        String IndividualDtoJson = FileUtil.readFromFileToString("TestIndividualDto.json");
        testIndividualDto = deserializeIndividualDto(IndividualDtoJson);
        kafkaTopic = "createIndividual";
        messageId = "ProfileLoader";
    }

    @Test
    public void sendMessageTest() {
        kafkaMessageSender.sendMessage(testIndividualDto);
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(kafkaTopic, messageId, testIndividualDto);
        Assertions.assertThat(testIndividualDto).isNotNull();
    }
}