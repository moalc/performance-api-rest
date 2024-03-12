package com.rok.faker.performanceapirest.entitys;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Player {

    @Id
    private Long id;

    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime initDate;

    private int vip;

    private boolean inKingdom = true;
}
