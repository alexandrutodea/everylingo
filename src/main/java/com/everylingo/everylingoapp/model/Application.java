package com.everylingo.everylingoapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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
    AppUser appUser;
    @Enumerated(EnumType.STRING)
    Status status;
}
