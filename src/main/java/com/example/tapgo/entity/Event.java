package com.example.tapgo.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Transactional
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="public",
            name="MySequenceGenerator", sequenceName="mysequence")
    private Long eventId;

    @Column
    private String eventName;

    @Column
    private String description;

    @Column
    private String photo;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToMany(mappedBy = "goList", fetch = FetchType.EAGER)
    private List<User> usersInGoList = new ArrayList<>();
}
