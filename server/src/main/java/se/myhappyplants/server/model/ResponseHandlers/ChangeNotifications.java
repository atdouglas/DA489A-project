package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.UserRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.User;

/**
 * Class that handles the change of the notifications
 */
public class ChangeNotifications implements IResponseHandler {
    private UserRepository userRepository;

    public ChangeNotifications(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        if (userRepository.changeNotifications(request.getUser().getEmail(), request.getNotifications())) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
