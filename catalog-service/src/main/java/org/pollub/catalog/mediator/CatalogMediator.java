package org.pollub.catalog.mediator;

import org.pollub.catalog.mediator.handler.GetItemsForReservationHandler;
import org.pollub.catalog.mediator.handler.ReserveItemCommandHandler;
import org.pollub.catalog.mediator.handler.UpdateItemInventoryStatusHandler;
import org.pollub.catalog.mediator.request.GetItemsForReservationRequest;
import org.pollub.catalog.mediator.request.MarkAsReservedRequest;
import org.pollub.catalog.mediator.request.UpdateItemInventoryStatusRequest;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.Request;
import org.pollub.common.mediator.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//Lab5 Mediator Start
@Component
public class CatalogMediator implements Mediator {

    private final Map<Class<?>, RequestHandler<?, ?>> handlers = new HashMap<>();

    public CatalogMediator(
            ReserveItemCommandHandler reserveItemCommandHandler,
            GetItemsForReservationHandler getItemsForReservationHandler,
            UpdateItemInventoryStatusHandler updateItemInventoryStatusHandler
    ) {
        handlers.put(MarkAsReservedRequest.class, reserveItemCommandHandler);
        handlers.put(GetItemsForReservationRequest.class, getItemsForReservationHandler);
        handlers.put(UpdateItemInventoryStatusRequest.class, updateItemInventoryStatusHandler);
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
