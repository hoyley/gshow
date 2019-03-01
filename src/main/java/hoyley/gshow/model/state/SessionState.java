package hoyley.gshow.model.state;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.GlobalState;

public class SessionState {
    private GlobalState globalState;
    private boolean isAdmin;
    private Player myPlayer;

    public GlobalState getGlobalState() {
        return globalState;
    }

    public void setGlobalState(GlobalState globalState) {
        this.globalState = globalState;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }
}
