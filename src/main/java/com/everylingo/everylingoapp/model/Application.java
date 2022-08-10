package com.everylingo.everylingoapp.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Application {
    @Id
    @SequenceGenerator(
            name = "application_gen",
            sequenceName = "application_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_gen"
    )
    private Long id;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "app_user_id_fk"), nullable = false)
    AppUser appUser;
    @Enumerated(EnumType.STRING)
    Status status;

    public Application(AppUser appUser) {
        this.appUser = appUser;
        this.status = Status.PENDING;
    }
}
