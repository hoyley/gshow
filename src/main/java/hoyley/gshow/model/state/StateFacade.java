package hoyley.gshow.model.state;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StateFacade {

    private final GlobalState state;
    private final Consumer<String> stateUpdated;
    private boolean delayUpdate = false;

    public StateFacade(GlobalState state, Consumer<String> stateUpdated) {
        this.state = state;
        this.stateUpdated = stateUpdated;
    }

    public void setChoiceGameState(ChoiceGameState choiceGameState) {
        state.setChoiceGameState(choiceGameState);
    }

    public void setScreen(GlobalState.Screen screen) {
        state.setScreen(screen);
    }

    public boolean isAdminActive() {
        return state.adminIsActive();
    }

    public List<String> getPlayerIds() {
        return state.getRegisteredPlayers().stream()
            .map(p -> p.getId())
            .collect(Collectors.toList());
    }

    public List<String> getPlayerSessionIds() {
        return state.getRegisteredPlayers().stream()
            .map(p -> p.getSessionId())
            .collect(Collectors.toList());
    }

    public void incrementPlayerScore(String playerId, int points) {
        state.getRegisteredPlayers().stream()
            .filter(player -> player.getId().equals(playerId))
            .forEach(player -> {
                player.setScore(player.getScore() + points);
            });

        publish();
    }

    public String getAdminSessionId() {
        return state.getAdminSessionId();
    }

    public void setAdminSessionId(String id) {
        state.setAdminSessionId(id);
        publish();
    }

    public boolean addPlayer(Player player) {
        // If player does not already exist with the given session ID, add the player. We don't want
        // the player to add themselves twice
        if (state.getRegisteredPlayers().stream()
            .anyMatch(p -> p.getSessionId().equals(player.getSessionId())) == false) {
            state.getRegisteredPlayers().add(player);

            publish();
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayerBySessionId(String sessionId) {
        boolean removed = state.getRegisteredPlayers().removeIf(p -> Objects.equals(sessionId, p.getSessionId()));

        if (removed) {
            publish();
        }
        return removed;
    }
    public boolean removePlayerById(String id) {
        boolean removed = state.getRegisteredPlayers().removeIf(p -> Objects.equals(id, p.getId()));

        if (removed) {
            publish();
        }
        return removed;
    }

    public void batch(Consumer<StateFacade> batchFunction) {
        try {
            delayUpdate = true;
            batchFunction.accept(this);
        } finally {
            delayUpdate = false;
            publish();
        }
    }

    public SessionState getSessionState(PlayerHelper helper) {
        Optional<Player> myPlayer = state.getRegisteredPlayers().stream()
            .filter(p -> Objects.equals(helper.getPlayerSessionId(), p.getSessionId()))
            .findFirst();

        return new SessionState() {{
            setAdmin(helper.isAdmin());
            setMyPlayer(myPlayer.orElseGet(() -> null));
            setGlobalState(state);
        }};
    }

    public void publish() {
        if (delayUpdate == false && stateUpdated != null) {
            getPlayerSessionIds().forEach(id -> {
                stateUpdated.accept(id);
            });

            if (getAdminSessionId() != null) {
                stateUpdated.accept(getAdminSessionId());
            }
        }
    }
}
