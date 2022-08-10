package com.everylingo.everylingoapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class TranslationRequest {
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "source_language_id_fk"))
    Language sourceLanguage;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "target_language_id_fk"))
    Language targetLanguage;

    public TranslationRequest(AppUser requestedBy, String sourceText, String translation, Language sourceLanguage, Language targetLanguage) {
        this.requestedBy = requestedBy;
        this.sourceText = sourceText;
        this.translation = translation;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }
}
