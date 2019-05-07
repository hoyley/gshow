package hoyley.gshow.service;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.state.GlobalState;
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
        GameService gameService = gameServiceFactory.create(new GlobalState());
        return new SessionService(sessionKey, adminKey, gameService, applicationEventPublisher);
    }
}
