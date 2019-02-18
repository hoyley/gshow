package hoyley.gshow.model;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RootState {

    private final List<Player> registeredPlayers = new LinkedList<>();
    private Screen screen;
    private Player myPlayer;

    public List<Player> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }
}
