package ru.itmo.yofik.webservices.back.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "avatars")
@Entity
public class Avatar {
    @Id
    private long studentId;
    @Column(columnDefinition = "TEXT")
    private String content;
}
