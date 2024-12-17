package com.example.tapgo.entity;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Transactional
@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="public",
            name="MySequenceGenerator", sequenceName="mysequence")
    private Long placeId;

    @Column
    private String placeName;

    @Column
    private String location;

    @Column
    private String category;

    @Column
    private String city;

    @Column
    private String photos;

    @Column
    private double averageRating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="events")
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;


    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;
}

