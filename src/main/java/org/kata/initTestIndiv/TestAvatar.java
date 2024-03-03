package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Avatar;
import org.kata.entity.Individual;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class TestAvatar extends Avatar {
    private final String uuid = UUID.randomUUID().toString();
    private final String filename = "avatarTestIndividual.jpg";
    private final String hex = null;
    private final byte[] imageData = convertImageToByteArray();
    private final ZonedDateTime uploadDate = ZonedDateTime.now();
    private Individual individual;
    private final boolean isActual = true;

    private byte[] convertImageToByteArray() {
        try (InputStream inputStream = Files.newInputStream(Path.of("src\\main\\resources\\static\\avatarTestIndividual.jpg"));
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Аватар не загрузился");
        }
    }
}
