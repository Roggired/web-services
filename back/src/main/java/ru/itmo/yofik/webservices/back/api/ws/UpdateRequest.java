package ru.itmo.yofik.webservices.back.api.ws;

import lombok.Data;

@Data
public class UpdateRequest {
    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private int age;
    private int height;
}
