package ru.itmo.yofik.webservices.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String patronymic;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private int height;
}
