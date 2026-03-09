package org.pollub.reservation.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.repository.ReservationRepository;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.common.dto.BookDto;
import org.pollub.reservation.client.CatalogServiceClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReservationMediator {
    private final ReservationRepository reservationRepository;
    private final CatalogServiceClient catalogServiceClient;
    private final NotificationService notificationService;
    private final AuditService auditService;

    //start L5 Mediator
    public boolean reserveBook(Long userId, Long bookId) {
        BookDto book = catalogServiceClient.getBookById(bookId);
        if (book == null || !book.isAvailable()) {
            notificationService.notifyUser(userId, "Książka nie jest dostępna.");
            return false;
        }

        long activeReservations = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.ACTIVE);
        if (activeReservations >= 3) {
            notificationService.notifyUser(userId, "Przekroczyłeś limit aktywnych rezerwacji.");
            return false;
        }

        ReservationHistory reservation = new ReservationHistory();
        reservationRepository.save(reservation);

        notificationService.notifyUser(userId, "Zarezerwowano książkę: " + book.getTitle());
        notificationService.notifyAdmin("Użytkownik " + userId + " zarezerwował książkę " + book.getTitle());

        auditService.logReservation(userId, bookId);

        return true;
    }
    //end L5 Mediator
}

