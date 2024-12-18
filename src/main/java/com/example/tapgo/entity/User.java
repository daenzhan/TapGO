package com.example.tapgo.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Transactional
@Table(name="users")
@Entity
public class User implements UserDetails{

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

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Place> places;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_event_go_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> goList;



    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
