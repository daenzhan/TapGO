package com.example.tapgo.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Transactional
@Table(name="users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="public",
            name="MySequenceGenerator", sequenceName="mysequence")
    private Long userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    @Email
    private String email;

    @Column
    private String profilePhoto;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name="is_admin",nullable = false)
    private boolean isAdmin=false;
    public String getRole() {
        return isAdmin ? "ADMIN" : "USER";
    }
    public void setRole(String role) {
        this.isAdmin = "ADMIN".equals(role);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;
}
