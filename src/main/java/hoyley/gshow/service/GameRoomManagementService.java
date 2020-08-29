package hoyley.gshow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class GameRoomManagementService {

    private ConcurrentHashMap<String, GameRoomService> gameRoomMap = new ConcurrentHashMap<>();
    private final GameRoomServiceFactory serviceFactory;

    @Autowired
    public GameRoomManagementService(GameRoomServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public GameRoomService getGameRoom(String gameRoomKey) {
        return gameRoomMap.computeIfAbsent(gameRoomKey, k -> serviceFactory.create(gameRoomKey));
    }

    public void forEach(Consumer<GameRoomService> callable) {
        gameRoomMap.values().forEach(callable);
    }
}
