package com.everylingo.everylingoapp.model;

import lombok.*;

import javax.persistence.*;


//@Entity

@Setter
@Getter
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
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
    private String language;
    private String name;

    public Language(String language, String name) {
        this.language = language;
        this.name = name;
    }
}