package org.kata.mapper;

import org.kata.mapper.config.MappersTestConfiguration;
import org.kata.mapper.config.SettersTestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig({MappersTestConfiguration.class, SettersTestConfiguration.class})
public interface MapperTest<Model, Dto>  {

    void shouldMapEntityToDto() throws IllegalAccessException;
    void shouldMapDtoToEntity() throws IllegalAccessException;
}
