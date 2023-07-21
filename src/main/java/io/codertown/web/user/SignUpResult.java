package io.codertown.web.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResult {
    private boolean success;
    private int code;
    private String msg;
}
