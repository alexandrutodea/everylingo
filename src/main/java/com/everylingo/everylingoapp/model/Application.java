package com.everylingo.everylingoapp.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
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
    @JoinColumn(foreignKey = @ForeignKey(name = "app_user_id_fk"))
    private AppUser appUser;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String message;

    public Application(AppUser appUser, String message) {
        this.message = message;
        this.appUser = appUser;
        this.status = Status.PENDING;
    }
}
