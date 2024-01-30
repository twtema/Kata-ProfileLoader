package org.kata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.kata.controller.dto.IndividualDto;
import org.kata.util.FileUtil;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.test.utils.KafkaTestUtils;


import java.io.IOException;
import java.util.Map;

import static org.kata.util.MapperUtil.deserializeIndividualDto;

@ExtendWith(MockitoExtension.class)
class KafkaMessageHandlerTest {
    private static final String CREATE_INDIVIDUAL_TOPIC = "createIndividual";
    private static final String MESSAGE_ID = "ProfileLoader";
    private static EmbeddedKafkaBroker embeddedKafka;
    private static Consumer<String, String> consumer;
    private static IndividualDto testIndividualDto;

    @BeforeAll
    static void setUp() throws IOException {
        String individualDtoJson = FileUtil.readFromFileToString("TestIndividualDto.json");
        testIndividualDto = deserializeIndividualDto(individualDtoJson);
        embeddedKafka = new EmbeddedKafkaBroker(1, true, CREATE_INDIVIDUAL_TOPIC);
        embeddedKafka.afterPropertiesSet();
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ProfileLoader", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
        consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterAll
    static void tearDown() {
        consumer.close();
        embeddedKafka.destroy();
    }

    @Test
    void handleMessage() throws JsonProcessingException {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.send(CREATE_INDIVIDUAL_TOPIC, MESSAGE_ID, testIndividualDto);


        ConsumerRecord<String, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, CREATE_INDIVIDUAL_TOPIC);
        IndividualDto dtoFromKafka = deserializeIndividualDto(consumerRecord.value());
        System.out.println(consumerRecord);

        Assertions.assertEquals(testIndividualDto, dtoFromKafka);
        Assertions.assertEquals(MESSAGE_ID, consumerRecord.key());

        kafkaTemplate.flush();
        kafkaTemplate.destroy();
    }
}