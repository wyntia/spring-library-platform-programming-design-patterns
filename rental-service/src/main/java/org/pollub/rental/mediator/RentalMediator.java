package org.pollub.rental.mediator;

import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.Request;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.mediator.handler.*;
import org.pollub.rental.mediator.request.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//Lab5 Mediator Start
@Component
public class RentalMediator implements Mediator {

    private final Map<Class<?>, RequestHandler<?, ?>> handlers = new HashMap<>();

    public RentalMediator(
            OverdueReminderHandler overdueReminderHandler,
            RentalConfirmationHandler rentalConfirmationHandler,
            ReturnConfirmationHandler returnConfirmationHandler,
            GetUserEmailHandler getUserEmailHandler,
            GetItemTitleHandler getItemTitleHandler,
            GetActiveRentalsHandler getActiveRentalsHandler,
            MarkAsRentedHandler markAsRentedHandler,
            MarkAsReturnedHandler markAsReturnedHandler,
            ExtendRentalHandler extendRentalHandler
    ) {
        handlers.put(SendOverdueReminderNotification.class, overdueReminderHandler);
        handlers.put(SendRentalConfirmationNotification.class, rentalConfirmationHandler);
        handlers.put(SendReturnConfirmationNotification.class, returnConfirmationHandler);
        handlers.put(GetUserEmailRequest.class, getUserEmailHandler);
        handlers.put(GetItemTitleRequest.class, getItemTitleHandler);
        handlers.put(GetActiveRentalsRequest.class, getActiveRentalsHandler);
        handlers.put(MarkAsRentedRequest.class, markAsRentedHandler);
        handlers.put(MarkAsReturnedRequest.class, markAsReturnedHandler);
        handlers.put(ExtendRentalRequest.class, extendRentalHandler);
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
