package hoyley.gshow.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.ChoiceGameState;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Wither;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Wither
public class GlobalState {

    public enum Screen {
        Welcome,
        ChoiceGame
    }

    @Singular private List<Player> registeredPlayers;
    @NonNull private ChoiceGameState choiceGameState;
    @NonNull private Screen screen;

    @JsonIgnore @NonNull private String adminSessionId;

    public void setAdminSessionId(String adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    public boolean adminIsActive() {
        return adminSessionId != null;
    }
}
