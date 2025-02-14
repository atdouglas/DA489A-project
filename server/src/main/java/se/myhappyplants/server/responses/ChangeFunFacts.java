package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.Message;

/**
 * Class that handles to change the fun facts
 */
public class ChangeFunFacts implements IResponseHandler {
    private UserRepository userRepository;

    public ChangeFunFacts (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Message getResponse(Message request) {
        Message response;
        if (userRepository.changeFunFacts(request.getUser().getEmail(), request.getNotifications())) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}