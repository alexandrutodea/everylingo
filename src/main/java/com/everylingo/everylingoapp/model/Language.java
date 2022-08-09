package com.everylingo.everylingoapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("language")
    private String code;
    @JsonProperty("name")
    private String name;

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
    }
}