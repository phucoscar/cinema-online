package com.vukimphuc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "film")
@Data
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "film_type",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id"))
    private List<Type> types;

    @Column(name = "description")
    private String description;

    @Column(name = "releaseDate")
    private Date releaseDate;

    @Column(name = "duration")
    private Integer duration;

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    private List<Thumnail> thumnails;

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    private List<Rating> ratings;
}