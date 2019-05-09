package hoyley.gshow.service;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.StateFacade;

import java.util.Objects;
import java.util.UUID;

public class SessionService {

    private final String sessionKey;
    private final String adminSecretKey;
    private final GameService gameService;
    private final StateFacade state;

    public SessionService(String sessionKey,
                          StateFacade state,
                          String adminKey,
                          GameService gameService) {
        this.sessionKey = sessionKey;
        this.adminSecretKey = adminKey;
        this.gameService = gameService;
        this.state = state;
    }

    public GameService getGame() {
        return gameService;
    }

    public StateFacade getState() {
        return state;
    }

    public boolean registerAdmin(String adminSecretKey, String adminSessionId) {
        if (Objects.equals(adminSecretKey, this.adminSecretKey)) {
            state.setAdminSessionId(adminSessionId);
            return true;
        }

        return false;
    }

    public void logout(PlayerHelper playerHelper) {
        if (playerHelper.isAdmin()) {
            state.setAdminSessionId(null);
        }

        state.removePlayerBySessionId(playerHelper.getPlayerSessionId());
    }

    public Player registerPlayer(String playerName, String playerSessionId) {
        Player player = new Player() {{
            setSessionId(playerSessionId);
            setNickname(playerName);
            setId(UUID.randomUUID().toString());
        }};

        if (state.addPlayer(player)) {
            return player;
        } else {
            return null;
        }
    }
}
