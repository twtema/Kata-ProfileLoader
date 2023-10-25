package org.kata.mapper.config;

import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.*;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfiguration {

    @Bean
    public MapperChecker mapperChecker() {
        return new MapperChecker();
    }
    @Bean
    public AddressMapper addressMapper() {
        return Mappers.getMapper(AddressMapper.class);
    }
    @Bean
    public AvatarMapper avatarMapper() {
        return Mappers.getMapper(AvatarMapper.class);
    }
    @Bean
    public ContactMediumMapper contactMediumMapper() {
        return Mappers.getMapper(ContactMediumMapper.class);
    }
    @Bean
    public IndividualMapper individualMapper() {
        return Mappers.getMapper(IndividualMapper.class);
    }
    @Bean
    public DocumentMapper documentMapper() {
        return Mappers.getMapper(DocumentMapper.class);
    }

}
