package se.myhappyplants.server.responses;

import se.myhappyplants.shared.Message;

// TODO: rename? is not "ResponseHandler", is instead actually the response
/**
 * Interface makes sure all ResponseHandlers have at
 * least these methods
 */
public interface IResponseHandler {

    Message getResponse(Message request);
}

