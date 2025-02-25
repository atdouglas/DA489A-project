package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.Plant;

/**
 * Class that gets the plant details
 */

public class GetPlant implements IResponseHandler {
    private PlantRepository plantRepository;

    public GetPlant(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        try {
            int id = Integer.parseInt(request.getMessageText());
            Plant plantDetails = plantRepository.getPlantDetails(id);
            response = new Message(plantDetails, true);
        } catch (Exception e) {
            response = new Message(false);
            e.printStackTrace();
        }
        return response;
    }
}
