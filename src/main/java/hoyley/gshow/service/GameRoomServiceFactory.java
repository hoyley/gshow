package hoyley.gshow.service;

import hoyley.gshow.model.state.GlobalState;
import hoyley.gshow.model.state.StateFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class GameRoomServiceFactory {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String adminKey;
    private final GameServiceFactory gameServiceFactory;

    @Autowired
    public GameRoomServiceFactory(ApplicationEventPublisher applicationEventPublisher,
                                  GameServiceFactory gameServiceFactory,
                                  @Value("${admin.key}") String adminKey) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.adminKey = adminKey;
        this.gameServiceFactory = gameServiceFactory;
    }

    public GameRoomService create(String sessionKey) {
        StateFacade state = new StateFacade(GlobalState.empty(), () ->
            applicationEventPublisher.publishEvent(sessionKey)
        );
        GameService gameService = gameServiceFactory.create(state);
        return new GameRoomService(sessionKey, state, adminKey, gameService);
    }
}
