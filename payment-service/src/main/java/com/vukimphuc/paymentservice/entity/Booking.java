package com.vukimphuc.paymentservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Ticket> tickets;
}
