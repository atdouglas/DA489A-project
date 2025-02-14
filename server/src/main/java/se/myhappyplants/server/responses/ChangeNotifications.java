package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.Message;

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
