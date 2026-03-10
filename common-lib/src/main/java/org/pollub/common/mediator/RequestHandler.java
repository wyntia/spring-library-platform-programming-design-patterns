package org.pollub.common.mediator;

//Lab5 Mediator Start
/**
 * Interface for handling a specific request type.
 *
 * @param <R> The type of the request that this handler can process.
 * @param <T> The type of the response that will be returned.
 */
public interface RequestHandler<R extends Request<T>, T> {
    T handle(R request);
}
//Lab5 Mediator End
