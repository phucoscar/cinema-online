package com.example.schedulesevice.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;
}