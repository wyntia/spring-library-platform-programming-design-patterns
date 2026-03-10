package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.CatalogServiceClient;
import org.pollub.rental.mediator.request.GetItemTitleRequest;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class GetItemTitleHandler implements RequestHandler<GetItemTitleRequest, String> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public String handle(GetItemTitleRequest request) {
        try {
            var item = catalogServiceClient.getItemById(request.itemId());
            return item != null ? item.getTitle() : "Nieznany tytuł";
        } catch (Exception e) {
            log.warn("Could not fetch title for item {}: {}", request.itemId(), e.getMessage());
            return "Nieznany tytuł";
        }
    }
}
//Lab5 Mediator End
