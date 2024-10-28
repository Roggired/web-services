package ru.itmo.yofik.webservices.back.api.ws;

import lombok.Data;

@Data
public class SetAvatarRequest {
    private long studentId;
    private String content;
}
