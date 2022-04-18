package com.ashimjk.jpatryout.optimisticlocking2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private final TicketRepository ticketRepository;

    public FlightService(FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    private void saveNewTicket(String firstName, String lastName, Flight flight) throws Exception {
        if (flight.getCapacity() <= flight.getTickets().size()) {
            throw new ExceededCapacityException();
        }
        var ticket = new Ticket();
        ticket.setFirstName(firstName);
        ticket.setLastName(lastName);
        flight.addTicket(ticket);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void changeFlight1WithOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findWithLockingById(flightId).orElseThrow();
        saveNewTicket("Robert", "Smith", flight);
        Thread.sleep(1_000);
    }

    @Transactional
    public void changeFlight1WithoutOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findById(flightId).orElseThrow();
        saveNewTicket("Robert", "Smith", flight);
        Thread.sleep(1_000);
    }

    @Transactional
    public void changeFlight2WithOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findWithLockingById(flightId).orElseThrow();
        saveNewTicket("Kate", "Brown", flight);
        Thread.sleep(1_000);
    }

    @Transactional
    public void changeFlight2WithoutOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findById(flightId).orElseThrow();
        saveNewTicket("Kate", "Brown", flight);
        Thread.sleep(1_000);
    }

    @Transactional
    public void updateCapacity1WithOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findById(flightId).orElseThrow();
        flight.setCapacity(10);
        Thread.sleep(1_000);
    }

    @Transactional
    public void updateCapacity2WithOptimisticLocking(Long flightId) throws Exception {
        var flight = flightRepository.findById(flightId).orElseThrow();
        flight.setCapacity(20);
        Thread.sleep(1_000);
    }

}