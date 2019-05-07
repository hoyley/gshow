package hoyley.gshow.helpers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.GlobalState;

import java.util.Objects;

public class PlayerHelper {
    private final GlobalState state;
    private final Func playerId;

    public PlayerHelper(String playerId, GlobalState state) {
        this(() -> playerId, state);
    }

    public PlayerHelper(Func<String> playerId, GlobalState state) {
        this.state = state;
        this.playerId = playerId;
    }

    public Player getPlayer() {
        return state.getRegisteredPlayers().stream()
            .filter(p -> p.getSessionId().equals(playerId.get()))
            .findFirst().orElse(null);
    }

    public boolean isAdmin() {
        return Objects.equals(playerId.get(), state.getAdminSessionId());
    }

    public boolean adminIsPresent() {
        return state.getAdminSessionId() != null;
    }

    public boolean hasGameControl() {
        if (adminIsPresent()) {
            return isAdmin();
        } else {
            return true;
        }
    }

    public interface Func<T> {
        T get();
    }
}
