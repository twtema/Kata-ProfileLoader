package org.kata;

import org.kata.service.mapper.DocumentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfiguration {
    @Bean
    public DocumentMapper getDocumentMapper () {
        return Mappers.getMapper(DocumentMapper.class);
    }
}
