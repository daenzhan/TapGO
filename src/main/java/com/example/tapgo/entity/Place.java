package com.example.tapgo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
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
    private String photos;

    @Column(name="events")
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;


    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}

