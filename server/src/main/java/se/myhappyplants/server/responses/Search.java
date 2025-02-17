package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.Plant;

import java.util.List;

/**
 * Class that handles the request of a search
 */
public class Search implements IResponseHandler {
    private PlantRepository plantRepository;

    public Search(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        String searchText = request.getMessageText();
        try {
            response = new Message(plantRepository.getResult(searchText), true);
        } catch (Exception e) {
            response = new Message(false);
            e.printStackTrace();
        }
        return response;
    }
}
