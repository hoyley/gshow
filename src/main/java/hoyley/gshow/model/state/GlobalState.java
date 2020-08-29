package hoyley.gshow.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hoyley.gshow.model.Player;
import lombok.*;

import java.util.List;

@With
@Builder(toBuilder = true)
@Getter
public class GlobalState {

    public enum Screen {
        Welcome,
        ChoiceGame
    }

    @Singular private List<Player> registeredPlayers;
    @NonNull private ChoiceGameState choiceGameState;
    @NonNull private Screen screen;

    @JsonIgnore private String adminSessionId;

    public static GlobalState empty() {
        return GlobalState.builder()
            .choiceGameState(ChoiceGameState.builder().build())
            .screen(Screen.Welcome)
            .build();
    }
    public void setAdminSessionId(String adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    public boolean adminIsActive() {
        return adminSessionId != null;
    }
}
