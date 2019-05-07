package hoyley.gshow.service;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.GlobalState;
import hoyley.gshow.model.state.SessionState;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;
import java.util.UUID;

public class SessionService {

    private final String sessionKey;
    private final ApplicationEventPublisher publisher;
    private final String adminSecretKey;
    private final GameService gameService;

    public SessionService(String sessionKey,
                          String adminKey,
                          GameService gameService,
                          ApplicationEventPublisher publisher) {
        this.sessionKey = sessionKey;
        this.publisher = publisher;
        this.adminSecretKey = adminKey;
        this.gameService = gameService;
    }

    public GameService getGame() {
        return gameService;
    }

    public SessionState sessionState(PlayerHelper helper) {
        return new SessionState() {{
            setAdmin(helper.isAdmin());
            setMyPlayer(helper.getPlayer());
            setGlobalState(gameState());
        }};
    }

    public boolean registerAdmin(String adminSecretKey, String adminSessionId) {
        if (Objects.equals(adminSecretKey, this.adminSecretKey)) {
            gameState().setAdminSessionId(adminSessionId);
            publish();
            return true;
        }

        return false;
    }

    public void logout(PlayerHelper playerHelper) {
        boolean publish = false;

        if (playerHelper.isAdmin()) {
            gameState().setAdminSessionId(null);
            publish = true;
        }

        if (playerHelper.getPlayer() != null) {
            gameState().getRegisteredPlayers().remove(playerHelper.getPlayer());
            publish = true;
        }

        if (publish) {
            publish();
        }
    }

    public Player registerPlayer(String playerName, String playerSessionId) {
        Player player = new Player() {{
            setSessionId(playerSessionId);
            setNickname(playerName);
            setId(UUID.randomUUID().toString());
        }};

        // If player does not already exist with the given session ID, add the player. We don't want
        // the player to add themselves twice
        if (gameState().getRegisteredPlayers().stream()
                .anyMatch(p -> p.getSessionId().equals(playerSessionId)) == false) {
            gameState().getRegisteredPlayers().add(player);
            publish();

            return player;
        } else {
            return null;
        }
    }

    private GlobalState gameState() {
        return gameService.getState();
    }

    private void publish() {
        gameState().getRegisteredPlayers().stream()
            .map(p -> p.getSessionId())
            .forEach(sid -> publisher.publishEvent(sid));
    }
}
