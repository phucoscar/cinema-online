package com.vukimphuc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "thumnail")
@Data
@NoArgsConstructor
public class Thumnail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "source")
    private String src;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;
}
