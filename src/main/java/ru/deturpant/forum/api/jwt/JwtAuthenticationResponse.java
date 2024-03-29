package ru.deturpant.forum.api.jwt;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class JwtAuthenticationResponse {

    private String accessToken;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


}
