package ru.deturpant.forum.api.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegRequest {
    private String username;
    private String password;
}
