package org.kata.mapper.setters;

import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;

import java.util.Random;


public class AvatarSetter implements Setter<Avatar, AvatarDto> {

    @Override
    public void setEntityFields(Avatar avatar) {
        StringFieldsSetterUtil.set(avatar);
        avatar.setImageData(generateByteArray());
    }

    @Override
    public void setDtoFields(AvatarDto avatarDto) {
        StringFieldsSetterUtil.set(avatarDto);
        avatarDto.setImageData(generateByteArray());
    }

    private byte[] generateByteArray() {
        byte[] b = new byte[100];
        new Random().nextBytes(b);
        return b;
    }
}
