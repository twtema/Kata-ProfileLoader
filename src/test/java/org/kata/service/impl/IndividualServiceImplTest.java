package org.kata.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.KafkaMessageSender;
import org.kata.service.mapper.IndividualMapper;
import org.kata.util.FileUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.kata.util.MapperUtil.deserializeIndividual;
import static org.kata.util.MapperUtil.deserializeIndividualDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class IndividualServiceImplTest {

    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private IndividualCrudRepository individualCrudRepository;

    @Mock
    KafkaMessageSender kafkaMessageSender;

    @InjectMocks
    private IndividualServiceImpl individualServiceImpl;

    private IndividualDto testIndividualDto;
    private Individual testIndividual;

    @Before
    public void setUp() throws IOException {
        String IndividualDtoJson = FileUtil.readFromFileToString("TestIndividualDto.json");
        testIndividualDto = deserializeIndividualDto(IndividualDtoJson);
        String IndividualJson = FileUtil.readFromFileToString("TestIndividual.json");
        testIndividual = deserializeIndividual(IndividualJson);
    }

    @Test
    public void saveIndividual() {
        when(individualMapper.toEntity(Mockito.any(IndividualDto.class))).thenReturn(testIndividual);
        when(individualCrudRepository.save(Mockito.any(Individual.class))).thenReturn(testIndividual);
        individualServiceImpl.saveIndividual(testIndividualDto);

        Mockito.verify(individualCrudRepository, Mockito.times(1)).save(testIndividual);

        Assertions.assertThat(testIndividual).isNotNull();
        Assertions.assertThat(testIndividualDto).isNotNull();
    }

    @Test
    public void saveIndividualAndSendMessage() {
        kafkaMessageSender.sendMessage(testIndividualDto);
        Mockito.verify(kafkaMessageSender, Mockito.times(1)).sendMessage(testIndividualDto);
    }

}