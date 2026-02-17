package com.cdwater.movietimeuser.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendCodeDTO {

    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;

    @Pattern(regexp = "^(login|forget-pwd)$")
    private String codeType;
}
