package com.everylingo.everylingoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "language_code_unique", columnNames = "code"), @UniqueConstraint(name = "language_name_unique", columnNames = "name")})
@NoArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_gen")
    @SequenceGenerator(name = "language_gen", sequenceName = "language_seq", allocationSize = 1)
    private Long id;
    @JsonProperty("language")
    @Column(name = "code", nullable = false)
    private String code;
    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToMany(mappedBy = "preferredLanguages")
    @JsonIgnore
    List<AppUser> preferredBy;

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
        this.preferredBy = new ArrayList<>();
    }
}