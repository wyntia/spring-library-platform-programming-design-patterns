package org.pollub.rental.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.mediator.Mediator;
import org.pollub.rental.mediator.request.SendOverdueReminderNotification;
import org.pollub.rental.model.RentalHistory;
import org.pollub.rental.model.RentalStatus;
import org.pollub.rental.repository.IRentalHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Service for sending rental reminder emails to users
 * whose rentals expire in 3 days.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RentalReminderService {

    private static final int DAYS_BEFORE_DUE = 3;

    //Lab5 Mediator Start
    private final IRentalHistoryRepository rentalHistoryRepository;
    private final Mediator mediator;

    /**
     * Find all rentals expiring in 3 days and send reminder notifications via Mediator.
     */
    public void sendReminders() {
        log.info("Starting rental reminder job...");

        LocalDate targetDate = LocalDate.now().plusDays(DAYS_BEFORE_DUE);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<RentalHistory> expiringRentals = rentalHistoryRepository
                .findByDueDateBetweenAndStatus(startOfDay, endOfDay, RentalStatus.RENTED);

        log.info("Found {} rentals expiring in {} days", expiringRentals.size(), DAYS_BEFORE_DUE);

        int successCount = 0;
        int failCount = 0;

        for (RentalHistory rental : expiringRentals) {
            try {
                mediator.send(new SendOverdueReminderNotification(rental.getId()));
                successCount++;
            } catch (Exception e) {
                log.error("Error processing reminder for rental {}: {}", rental.getId(), e.getMessage());
                failCount++;
            }
        }

        log.info("Rental reminder job completed. Sent: {}, Failed: {}", successCount, failCount);
    }
    //Lab5 Mediator End
}
