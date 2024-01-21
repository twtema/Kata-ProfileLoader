package org.kata.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.kata.controller.dto.IndividualDto;
import org.kata.util.FileUtil;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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

//    @Mock
//    private IndividualServiceImpl individualServiceImpl;
//
//    @InjectMocks
//    KafkaMessageHandler kafkaMessageHandler;

    private static String topic1 = "createIndividual";

    private static String topic2 = "testTopic2";

    private static String messageId = "ProfileLoader";

    private static EmbeddedKafkaBroker embeddedKafka;

    private static Consumer<String, String> consumer;

    private static String IndividualDtoJson;
    private static IndividualDto testIndividualDto;


    @BeforeAll
    static void setUp() throws IOException {
        IndividualDtoJson = FileUtil.readFromFileToString("TestIndividualDto.json");
        testIndividualDto = deserializeIndividualDto(IndividualDtoJson);

        embeddedKafka = new EmbeddedKafkaBroker(1, true, topic1, topic2);
        embeddedKafka.afterPropertiesSet();
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ProfileLoader", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
        consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAllEmbeddedTopics(consumer);

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(record -> record.key().contains(messageId));
    }

    @AfterAll
    static void tearDown() {
        consumer.close();
        embeddedKafka.destroy();
    }

    @Test
    void handleMessage() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);

        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.send(topic1, messageId, testIndividualDto);

        ConsumerRecord<String, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, topic1);
//        Assertions.assertEquals(IndividualDtoJson, consumerRecord.value());

        producerFactory.destroy();
    }



}