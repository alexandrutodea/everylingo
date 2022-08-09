package com.everylingo.everylingoapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "auth_provider_id_unique", columnNames = "auth_provider_id")
})
public class AppUser {
    @Id
    @SequenceGenerator(
            name = "app_user_gen",
            sequenceName = "app_user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_gen"
    )
    private Long id;
    @Column(name = "auth_provider_id", nullable = false)
    private String authProviderId;
    @Enumerated(EnumType.STRING)
    private AppUserRole role = AppUserRole.USER;
    private boolean enabled;
    @OneToMany(mappedBy = "requestedBy")
    private List<Request> requests;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "preferredLanguages",
            joinColumns = @JoinColumn(name = "app_user_id",
                    foreignKey = @ForeignKey(name = "app_user_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "language_id",
                    foreignKey = @ForeignKey(name = "language_id_fk")))
    Set<Language> preferredLanguages;
}