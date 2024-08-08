package com.example.producttestapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "my_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String firstName;
        private String lastName;
        private String password;
        private String email;
        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private Cart cart;

    public User(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role",
                joinColumns = @JoinColumn(name = "USER_ID"),
                inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
        )
        private Set<Role> roles;
   public void addRole(Role role){
            if(roles == null){
                roles = new HashSet<>();
            }
            roles.add(role);
        }

}
