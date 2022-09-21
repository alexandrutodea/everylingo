package com.everylingo.everylingoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "auth_provider_id_unique", columnNames = "auth_provider_id")})
public class AppUser {
    @Id
    @SequenceGenerator(name = "app_user_gen", sequenceName = "app_user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_gen")
    private Long id;
    @Column(name = "auth_provider_id", nullable = false)
    private String authProviderId;
    @Enumerated(EnumType.STRING)
    private AppUserRole role;
    private boolean enabled;
    @OneToMany(mappedBy = "requestedBy", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TranslationRequest> translationRequests;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "preferred_languages",
            joinColumns = @JoinColumn(name = "app_user_id",
                    foreignKey = @ForeignKey(name = "preferred_languages_app_user_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "language_id",
                    foreignKey = @ForeignKey(name = "preferred_languages_language_id_fk")))
    List<Language> preferredLanguages;

    public AppUser(Long id, String authProviderId) {
        this.id = id;
        this.authProviderId = authProviderId;
        this.role = AppUserRole.USER;
        this.translationRequests = new ArrayList<>();
        this.preferredLanguages = new ArrayList<>();
    }

    public AppUser(String authProviderId) {
        this.authProviderId = authProviderId;
        this.role = AppUserRole.USER;
        this.translationRequests = new ArrayList<>();
        this.preferredLanguages = new ArrayList<>();
    }

    public void addTranslationRequest(TranslationRequest request) {
        if (!this.translationRequests.contains(request)) {
            this.translationRequests.add(request);
            request.setRequestedBy(this);
        }
    }

    public void removeTranslationRequest(TranslationRequest request) {
        this.translationRequests.remove(request);
        request.setRequestedBy(null);
    }

    public void addPreferredLanguage(Language language) {
        if (!this.preferredLanguages.contains(language)) {
            preferredLanguages.add(language);
            language.getPreferredBy().add(this);
        }
    }

    public void removePreferredLanguage(Language language) {
        preferredLanguages.remove(language);
        language.getPreferredBy().remove(this);
    }
}