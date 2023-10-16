package org.kata.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AvatarDto {

    private String icp;

    private String filename;

    private byte[] imageData;
}
