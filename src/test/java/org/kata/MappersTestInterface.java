package org.kata;

import org.apache.catalina.mapper.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.service.mapper.DocumentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Inherited;




@SpringJUnitConfig(MappersTestConfiguration.class)
public interface MappersTestInterface <Model, Dto, Mapper>  {

    void shouldMapEntityToDto() throws IllegalAccessException;
    void shouldMapDtoToEntity();
    void setModelFields(Model model);
    void setDtoFields(Dto dto);


}
