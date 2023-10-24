package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;
import org.kata.service.mapper.AvatarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AvatarMapperTest implements MapperTest<Avatar, AvatarDto>{
    @Autowired
    private AvatarMapper avatarMapper;
    @Autowired
    private MapperChecker mapperChecker;

    private Avatar avatarFrom;
    private AvatarDto avatarDtoFrom;

    @Before
    public void setUp() {
        avatarFrom = new Avatar();
        avatarDtoFrom = avatarMapper.toDto(avatarFrom);
        setEntityFields(avatarFrom);
        setDtoFields(avatarDtoFrom);
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


    @Override
    public void setEntityFields(Avatar avatar) {
        avatar.setFilename("FromEntityFileName");
        avatar.setImageData(new byte[5]);
    }

    @Override
    public void setDtoFields(AvatarDto avatarDto) {
        avatarDto.setFilename("FromDtoFileName");
        avatarDto.setImageData(new byte[10]);
    }
}
