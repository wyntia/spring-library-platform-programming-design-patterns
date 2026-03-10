package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.UserServiceClient;
import org.pollub.rental.mediator.request.GetUserEmailRequest;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class GetUserEmailHandler implements RequestHandler<GetUserEmailRequest, String> {

    private final UserServiceClient userServiceClient;

    @Override
    public String handle(GetUserEmailRequest request) {
        return userServiceClient.getUserEmail(request.userId());
    }
}
//Lab5 Mediator End
