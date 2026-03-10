package org.pollub.common.mediator;

//Lab5 Mediator Start
/**
 * Mediator interface for dispatching requests to their registered handlers.
 */
public interface Mediator {
    /**
     * @param request The request that will be handled by its registered handler, if it exists.
     * @param <T>     The type of the response that will be returned.
     * @return The result of the executed handler.
     */
    <T> T send(Request<T> request);
}
//Lab5 Mediator End
