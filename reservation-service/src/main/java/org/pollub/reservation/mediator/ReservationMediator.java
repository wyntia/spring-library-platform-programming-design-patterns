package org.pollub.reservation.mediator;

import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.Request;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.mediator.handler.*;
import org.pollub.reservation.mediator.request.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//Lab5 Mediator Start
@Component
public class ReservationMediator implements Mediator {

    private final Map<Class<?>, RequestHandler<?, ?>> handlers = new HashMap<>();

    public ReservationMediator(
            CreateReservationHandler createReservationHandler,
            GetUserReservationsHandler getUserReservationsHandler,
            CancelReservationHandler cancelReservationHandler,
            FulfillReservationHandler fulfillReservationHandler,
            MarkCatalogAsReservedHandler markCatalogAsReservedHandler,
            UpdateCatalogStatusHandler updateCatalogStatusHandler,
            GetCatalogItemsInfoHandler getCatalogItemsInfoHandler
    ) {
        handlers.put(CreateReservationRequest.class, createReservationHandler);
        handlers.put(GetUserReservationsRequest.class, getUserReservationsHandler);
        handlers.put(CancelReservationRequest.class, cancelReservationHandler);
        handlers.put(FulfillReservationRequest.class, fulfillReservationHandler);
        handlers.put(MarkCatalogAsReservedRequest.class, markCatalogAsReservedHandler);
        handlers.put(UpdateCatalogStatusRequest.class, updateCatalogStatusHandler);
        handlers.put(GetCatalogItemsInfoRequest.class, getCatalogItemsInfoHandler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T send(Request<T> request) {
        RequestHandler<Request<T>, T> handler = (RequestHandler<Request<T>, T>) handlers.get(request.getClass());
        if (handler == null) {
            throw new IllegalArgumentException(
                    "No handler registered for request type: " + request.getClass().getName()
            );
        }
        return handler.handle(request);
    }
}
//Lab5 Mediator End
