package com.ashimjk.jpatryout.optimisticlocking2;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "flights")
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private LocalDateTime departureTime;

    private Integer capacity;

    @OneToMany(mappedBy = "flight")
    private Set<Ticket> tickets;

    @Version
    private Long version;

    public void addTicket(Ticket ticket) {
        if (tickets == null)
            tickets = new HashSet<>();

        ticket.setFlight(this);
        getTickets().add(ticket);
    }

}