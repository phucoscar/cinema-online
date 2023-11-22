package com.vukimphuc.paymentservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ticket_class")
    private Integer ticketClass;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "seat_number_horizontal")
    private Integer seatNumberHorizontal;

    @Column(name = "seat_number_vertical")
    private Integer seatNumberVertical;
}
