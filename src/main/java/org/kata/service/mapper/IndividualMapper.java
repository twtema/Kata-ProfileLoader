package org.kata.service.mapper;

import org.kata.controller.dto.AvatarDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Avatar;
import org.kata.entity.Individual;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IndividualMapper {

    Individual toEntity(IndividualDto individualDto);

    IndividualDto toDto(Individual individual);

    default Avatar avatarDtoToAvatar(AvatarDto avatarDto) {
        if (avatarDto == null) {
            return null;
        } else {
            Avatar avatar = new Avatar();
            avatar.setFilename(avatarDto.getFilename());
            byte[] imageData = avatarDto.getImageData();
            if (imageData != null) {
                avatar.setImageData(Arrays.copyOf(imageData, imageData.length));
            }
            avatar.setHex(SHAsum(imageData));
            return avatar;
        }
    }

    private String SHAsum(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return byteArray2Hex(md.digest(convertme));
    }

    private String byteArray2Hex(final byte[] hash) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}