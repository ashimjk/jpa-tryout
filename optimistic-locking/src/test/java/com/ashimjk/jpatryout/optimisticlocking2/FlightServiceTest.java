package com.ashimjk.jpatryout.optimisticlocking2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightServiceTest {

    @Autowired private FlightService flightService;
    @Autowired private FlightRepository flightRepository;
    @Autowired private TicketRepository ticketRepository;

    private Long flightId;

    @BeforeEach
    void setup() {
        ticketRepository.deleteAll();
        flightRepository.deleteAll();

        Flight flight = new Flight();
        flight.setNumber("FLT123");
        flight.setDepartureTime(LocalDateTime.now());
        flight.setCapacity(2);

        flightId = flightRepository.saveAndFlush(flight).getId();

        Ticket ticket = new Ticket();
        ticket.setFirstName("Paul");
        ticket.setLastName("Lee");
        flight.addTicket(ticket);
        ticketRepository.saveAndFlush(ticket);
        flightRepository.saveAndFlush(flight);
    }

    @Test
    void saveNewTicket_withoutOptimisticLockingHandling() throws InterruptedException {
        // When
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(safeRunnable(() -> flightService.changeFlight1WithoutOptimisticLocking(flightId)));
        executor.execute(safeRunnable(() -> flightService.changeFlight2WithoutOptimisticLocking(flightId)));

        executor.shutdown();
        boolean awaitTermination = executor.awaitTermination(1, TimeUnit.MINUTES);

        assertAll(
                () -> assertTrue(awaitTermination),
                () -> assertEquals(1, flightRepository.count()),
                () -> assertEquals(2, flightRepository.findById(flightId).orElseThrow().getCapacity()),
                () -> assertEquals(3, ticketRepository.count())
        );
    }

    @Test
    void saveNewTicket_withOptimisticLockingHandling() throws InterruptedException {
        // When
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(safeRunnable(() -> flightService.changeFlight1WithOptimisticLocking(flightId)));
        executor.execute(safeRunnable(() -> flightService.changeFlight2WithOptimisticLocking(flightId)));

        executor.shutdown();
        boolean awaitTermination = executor.awaitTermination(1, TimeUnit.MINUTES);

        assertAll(
                () -> assertTrue(awaitTermination),
                () -> assertEquals(1, flightRepository.count()),
                () -> assertEquals(2, flightRepository.findById(flightId).orElseThrow().getCapacity()),
                () -> assertEquals(2, ticketRepository.count())
        );
    }

    @Test
    void updateFlightCapacity_withOptimisticLockingHandling() throws InterruptedException {
        // When
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(safeRunnable(() -> flightService.updateCapacity1WithOptimisticLocking(flightId)));
        executor.execute(safeRunnable(() -> flightService.updateCapacity2WithOptimisticLocking(flightId)));

        executor.shutdown();
        boolean awaitTermination = executor.awaitTermination(1, TimeUnit.MINUTES);

        assertAll(
                () -> assertTrue(awaitTermination),
                () -> assertEquals(1, flightRepository.count()),
                () -> assertEquals(10, flightRepository.findById(flightId).orElseThrow().getCapacity())
        );
    }

    private Runnable safeRunnable(FailableRunnable<Exception> runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @FunctionalInterface
    public interface FailableRunnable<T extends Throwable> {

        void run() throws T;

    }

}