package org.kata.mapper.setters;

import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;

import java.util.Random;
import java.util.stream.Stream;


public class AvatarSetter implements Setter {

    @Override
    public void setEntityFields(Object avatarObject) {
        Avatar avatar = (Avatar) avatarObject;
        StringFieldsSetterUtil.set(avatarObject);
        avatar.setImageData(generateByteArray());
    }

    @Override
    public void setDtoFields(Object avatarDtoObject) {
        AvatarDto avatarDto = (AvatarDto) avatarDtoObject;
        StringFieldsSetterUtil.set(avatarDtoObject);
        avatarDto.setImageData(generateByteArray());
    }

    private byte[] generateByteArray() {
        byte[] b = new byte[100];
        new Random().nextBytes(b);
        return b;
    }
}
