package hoyley.gshow.model.state;

import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateFacade {

    private final Runnable stateUpdated;
    private GlobalState state;
    private boolean delayUpdate = false;

    public StateFacade(GlobalState state, Runnable stateUpdated) {
        this.state = state;
        this.stateUpdated = stateUpdated;
    }

    public synchronized void setChoiceGameState(ChoiceGameState choiceGameState) {
        synchronized (this) {
            state = state.withChoiceGameState(choiceGameState);
        }

        publish();
    }

    public void setScreen(GlobalState.Screen screen) {
        synchronized (this) {
            state = state.withScreen(screen);
        }

        publish();
    }

    public void incrementPlayerScore(String playerId, int points) {
        synchronized (this) {
            state = state.withRegisteredPlayers(
                state.getRegisteredPlayers().stream()
                    .map(player -> Objects.equals(player.getSessionId(), playerId)
                            ? player.withScore(player.getScore() + points)
                            : player)
                    .collect(Collectors.toList())
            );
        }
        
        publish();
    }

    public synchronized void setAdminSessionId(String id) {
        synchronized (this) {
            state = state.withAdminSessionId(id);
        }

        publish();
    }

    public boolean addPlayer(Player player) {
        boolean publish = false;

        synchronized (this) {
            // If player does not already exist with the given session ID, add the player. We don't want
            // the player to add themselves twice
            if (state.getRegisteredPlayers().stream()
                .anyMatch(p -> p.getSessionId().equals(player.getSessionId())) == false) {

                state = state.withRegisteredPlayers(
                    Stream.concat(state.getRegisteredPlayers().stream(), Stream.of(player))
                        .collect(Collectors.toList())
                );

                publish = true;
            }
        }

        if (publish) {
            publish();
        }
        return publish;
    }

    public boolean removePlayerBySessionId(String sessionId) {
        return removePlayer(p -> Objects.equals(p.getSessionId(), sessionId));
    }

    public boolean removePlayer(Predicate<Player> playerMatch) {
        boolean removed = false;

        synchronized (this) {
            if (state.getRegisteredPlayers().stream().filter(playerMatch).findFirst().isPresent()) {
                List<Player> newRegisteredPlayers = new LinkedList<>(state.getRegisteredPlayers());
                newRegisteredPlayers.removeIf(playerMatch);
                state.withRegisteredPlayers(newRegisteredPlayers);
                removed = true;
            }
        }

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

    public String getAdminSessionId() {
        return state.getAdminSessionId();
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

    public void publish() {
        if (delayUpdate == false && stateUpdated != null) {
            stateUpdated.run();
        }
    }
}
