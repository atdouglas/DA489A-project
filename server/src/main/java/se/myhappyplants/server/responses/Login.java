package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.User;

/**
 * Class that handles the procedure when a user logs in
 */
public class Login implements IResponseHandler {
    private UserRepository userRepository;

    public Login(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;

        User user = request.getUser();
        String email = user.getEmail();
        String password = user.getPassword();

        if (userRepository.checkLogin(email, user.getPassword())) {
            user = userRepository.getUserDetails(email);
            user.setAccessToken(userRepository.getNewAccessToken(email, password));
            response = new Message(user, true);

        } else {
            response = new Message(false);
        }
        return response;
    }
}
