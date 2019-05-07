package hoyley.gshow.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.ChoiceGameState;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

public class GlobalState {

    public enum Screen {
        Welcome,
        ChoiceGame
    }

    private final List<Player> registeredPlayers = new LinkedList<>();
    private ChoiceGameState choiceGameState = new ChoiceGameState();
    private Screen screen = Screen.Welcome;

    @JsonIgnore
    private String adminSessionId;

    public List<Player> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public ChoiceGameState getChoiceGameState() {
        return choiceGameState;
    }

    public void setChoiceGameState(ChoiceGameState choiceGameState) {
        this.choiceGameState = choiceGameState;
    }

    public String getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(String adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    public boolean adminIsActive() {
        return adminSessionId != null;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
