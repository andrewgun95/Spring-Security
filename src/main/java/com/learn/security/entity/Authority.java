package com.learn.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Builder.Default
    @Column(name = "role")
    private String role = "ADMIN";

    @ManyToMany
    @JoinTable( //
            name = "user_authorities", //
            joinColumns = @JoinColumn(name = "role_id"), //
            inverseJoinColumns = @JoinColumn(name = "user_id") //
    )
    @Singular
    private Set<User> users;

}
