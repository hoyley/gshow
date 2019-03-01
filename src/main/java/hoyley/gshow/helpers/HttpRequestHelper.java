package hoyley.gshow.helpers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.GlobalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class HttpRequestHelper {

    private GlobalState state;

    private HttpServletRequest request;
    private boolean adminIsGameController;

    @Autowired
    public HttpRequestHelper(HttpServletRequest request, GlobalState state,
                             @Value("${admin.isGameController}") boolean adminIsGameController) {
        this.request = request;
        this.adminIsGameController = adminIsGameController;
        this.state = state;
    }

    public Player getPlayer() {
        final String sessionId = this.request.getSession().getId();

        return state.getRegisteredPlayers().stream()
            .filter(p -> p.getSessionId().equals(sessionId))
            .findFirst().orElse(null);
    }

    public boolean isAdmin() {
        return Objects.equals(this.request.getSession().getId(), state.getAdminSessionId());
    }

    public boolean adminIsPresent() {
        return state.getAdminSessionId() != null;
    }

    public boolean hasGameControl() {
        if (adminIsGameController && adminIsPresent()) {
            return isAdmin();
        } else {
            return true;
        }
    }
}
