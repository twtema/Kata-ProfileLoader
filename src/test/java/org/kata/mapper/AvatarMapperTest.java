package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.AvatarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AvatarMapperTest implements MapperTest<Avatar, AvatarDto>{
    @Autowired
    private AvatarMapper avatarMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("avatarSetter")
    private Setter setter;

    private Avatar avatarFrom;
    private AvatarDto avatarDtoFrom;

    @Before
    public void setUp() {
        avatarFrom = new Avatar();
        avatarDtoFrom = avatarMapper.toDto(avatarFrom);
        setter.setEntityFields(avatarFrom);
        setter.setDtoFields(avatarDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                avatarFrom,
                avatarMapper.toDto(avatarFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                avatarDtoFrom,
                avatarMapper.toEntity(avatarDtoFrom));
    }



}
