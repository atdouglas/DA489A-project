package se.myhappyplants.server.responses;

import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.MessageType;

import java.util.HashMap;

/**
 * Class that stores all the different handlers for database requests
 * Created by: Christopher O'Driscoll
 */
public class ResponseContext {

    private HashMap<MessageType, IResponseHandler> responders = new HashMap<>();

    public ResponseContext(UserRepository userRepository, UserPlantRepository userPlantRepository, PlantRepository plantRepository) {
        createResponders(userRepository, userPlantRepository, plantRepository);
    }

    /**
     * Links the relevant ResponseHandlers to each MessageType
     */
    private void createResponders(UserRepository userRepository, UserPlantRepository userPlantRepository, PlantRepository plantRepository) {
        //responders.put(MessageType.changeAllToWatered, new ChangeAllToWatered(userPlantRepository));
        responders.put(MessageType.changeFunFacts, new ChangeFunFacts(userRepository));
        //responders.put(MessageType.changeLastWatered, new ChangeLastWatered(userPlantRepository));
        //responders.put(MessageType.changeNickname, new ChangeNickname(userPlantRepository));
        responders.put(MessageType.changeNotifications, new ChangeNotifications(userRepository));
        //responders.put(MessageType.changePlantPicture, new ChangePlantPicture(userPlantRepository));
        responders.put(MessageType.deleteAccount, new DeleteAccount(userRepository));
        //responders.put(MessageType.deletePlant, new DeletePlant(userPlantRepository));
        //responders.put(MessageType.getLibrary, new GetLibrary(userPlantRepository));
        //responders.put(MessageType.getMorePlantInfo, new GetPlantDetails(plantRepository));
        responders.put(MessageType.login, new Login(userRepository));
        responders.put(MessageType.register, new Register(userRepository));
        //responders.put(MessageType.savePlant, new SavePlant(userPlantRepository));
        responders.put(MessageType.search, new Search(plantRepository));
    }

    public IResponseHandler getResponseHandler(MessageType messageType) {
        return responders.get(messageType);
    }
}
