package org.kata.mapper;

import org.kata.mapper.config.MappersTestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig(MappersTestConfiguration.class)
public interface MapperTest<Model, Dto>  {

    void shouldMapEntityToDto() throws IllegalAccessException;
    void shouldMapDtoToEntity() throws IllegalAccessException;
    void setEntityFields(Model model);
    void setDtoFields(Dto dto);


}
