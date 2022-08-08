package com.everylingo.everylingoapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Language {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "language_gen"
    )
    @SequenceGenerator(
            name = "language_gen",
            sequenceName = "language_seq",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private String code;
}
