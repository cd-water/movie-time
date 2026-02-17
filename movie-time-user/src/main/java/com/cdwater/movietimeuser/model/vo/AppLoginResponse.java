package com.cdwater.movietimeuser.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppLoginResponse {

    private String accessToken;

    private String refreshToken;
}
