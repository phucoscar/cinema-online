package com.vukimphuc.paymentservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "score_avg")
    private Float score;

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Thumnail> thumnails;

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Rating> ratings;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "lastModifyAt")
    private LocalDateTime lastModifyAt;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "lastModifyBy")
    private String lastModifyBy;
}
