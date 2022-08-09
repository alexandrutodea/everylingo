package com.everylingo.everylingoapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Request {
    @Id
    @SequenceGenerator(
            name = "request_gen",
            sequenceName = "request_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_gen"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "requested_by_id_fk"))
    AppUser requestedBy;
    @Column(nullable = false)
    String sourceText;
    String translation;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "source_language_id_fk"))
    Language sourceLanguage;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "target_language_id_fk"))
    Language targetLanguage;
}
