package com.cdwater.movietimeuser.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppLoginResponse {

    private String nickname;

    private String avatar;

    private String accessToken;

    private String refreshToken;
}
