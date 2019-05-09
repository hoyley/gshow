package hoyley.gshow.helpers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.StateFacade;

import java.util.Objects;

public class PlayerHelper {
    private final StateFacade state;
    private final Func playerSessionId;

    public PlayerHelper(String playerSessionId, StateFacade state) {
        this(() -> playerSessionId, state);
    }

    public PlayerHelper(Func<String> playerSessionId, StateFacade state) {
        this.state = state;
        this.playerSessionId = playerSessionId;
    }

    public String getPlayerSessionId() {
        return (String)playerSessionId.get();
    }

    public Player getPlayer() {
        return state.getSessionState(this).getMyPlayer();
    }

    public boolean isAdmin() {
        return Objects.equals(playerSessionId.get(), state.getAdminSessionId());
    }

    public boolean hasGameControl() {
        if (state.isAdminActive()) {
            return isAdmin();
        } else {
            return true;
        }
    }

    public interface Func<T> {
        T get();
    }
}
