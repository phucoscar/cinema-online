package com.vukimphuc.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    private List<User> user;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "booking_time")
    private Date bookingTime;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;
}
