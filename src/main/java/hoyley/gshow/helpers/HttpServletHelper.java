package hoyley.gshow.helpers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpServletHelper {

    private RootState state;

    @Autowired
    public HttpServletHelper(RootState state) {
        this.state = state;
    }

    public Player getPlayer(HttpServletRequest request) {
        return state.getRegisteredPlayers().stream()
            .filter(p -> p.getSessionId().equals(request.getSession().getId()))
            .findFirst().orElse(null);
    }
}
