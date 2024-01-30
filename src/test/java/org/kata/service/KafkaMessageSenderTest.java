package org.kata.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.util.FileUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;

import static org.kata.util.MapperUtil.deserializeIndividualDto;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageSenderTest {
    private static final String KAFKA_TOPIC = "createIndividual";

    private static final String MESSAGE_ID = "ProfileLoader";

    private IndividualDto testIndividualDto;

    private ListenableFuture<SendResult<String, Object>> mockListenableFuture;

    private SendResult<String, Object> mockSendResult;

    private ListenableFutureCallback<SendResult<String, Object>> mockListenableFutureCallback;

    @Mock
    private KafkaTemplate<String, Object> mockKafkaTemplate;

    @InjectMocks
    private KafkaMessageSender kafkaMessageSender;

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(kafkaMessageSender, "kafkaTopic", "createIndividual");
        ReflectionTestUtils.setField(kafkaMessageSender, "messageId", "ProfileLoader");
        String IndividualDtoJson = FileUtil.readFromFileToString("TestIndividualDto.json");
        testIndividualDto = deserializeIndividualDto(IndividualDtoJson);

        mockListenableFuture = mock(ListenableFuture.class);
        mockSendResult = mock(SendResult.class);
        mockListenableFutureCallback = mock(ListenableFutureCallback.class);
    }

    @Test
    void sendMessageTest_onSuccess_isCalled() {
        when(mockKafkaTemplate.send(KAFKA_TOPIC, MESSAGE_ID, testIndividualDto)).thenReturn(mockListenableFuture);

        doAnswer(invocationOnMock -> {
            ListenableFutureCallback<SendResult<String, Object>> callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockListenableFuture).addCallback(any(ListenableFutureCallback.class));
        mockListenableFuture.addCallback(mockListenableFutureCallback);
        kafkaMessageSender.sendMessage(testIndividualDto);

        Mockito.verify(mockListenableFutureCallback).onSuccess(mockSendResult);
        Mockito.verify(mockKafkaTemplate, Mockito.times(1)).send(KAFKA_TOPIC, MESSAGE_ID, testIndividualDto);
        Assertions.assertThat(testIndividualDto).isNotNull();
    }

    @Test
    void sendMessageTest_onFailure_isCalled() {
        RuntimeException ex = new RuntimeException("error");
        when(mockKafkaTemplate.send(KAFKA_TOPIC, MESSAGE_ID, testIndividualDto)).thenReturn(mockListenableFuture);

        doAnswer(invocationOnMock -> {
            ListenableFutureCallback<SendResult<String, Object>> callback = invocationOnMock.getArgument(0);
            callback.onFailure(ex);
            return null;
        }).when(mockListenableFuture).addCallback(any(ListenableFutureCallback.class));
        mockListenableFuture.addCallback(mockListenableFutureCallback);
        kafkaMessageSender.sendMessage(testIndividualDto);

        Mockito.verify(mockListenableFutureCallback).onFailure(ex);
        Mockito.verify(mockKafkaTemplate, Mockito.times(1)).send(KAFKA_TOPIC, MESSAGE_ID, testIndividualDto);
    }
}