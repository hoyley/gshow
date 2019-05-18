package hoyley.gshow.service;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.state.GlobalState;
import hoyley.gshow.model.state.StateFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SessionServiceFactory {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String adminKey;
    private final GameServiceFactory gameServiceFactory;

    @Autowired
    public SessionServiceFactory(ApplicationEventPublisher applicationEventPublisher,
                                 GameServiceFactory gameServiceFactory,
                                 @Value("${admin.key}") String adminKey) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.adminKey = adminKey;
        this.gameServiceFactory = gameServiceFactory;
    }

    public SessionService create(String sessionKey) {
        StateFacade state = new StateFacade(GlobalState.builder().build(), (playerSessionKey) ->
            applicationEventPublisher.publishEvent(playerSessionKey)
        );
        GameService gameService = gameServiceFactory.create(state);
        return new SessionService(sessionKey, state, adminKey, gameService);
    }
}
