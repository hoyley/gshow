package hoyley.gshow.controllers;

import hoyley.gshow.Configurator;
import hoyley.gshow.helpers.HttpRequestHelper;
import hoyley.gshow.model.state.GlobalState;
import hoyley.gshow.model.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GameShowController {

    private final GlobalState state;
    private final HttpRequestHelper servletHelper;

    @Autowired
    public GameShowController(GlobalState state, Configurator configurator, HttpRequestHelper servletHelper) {
        this.state = state;
        this.servletHelper = servletHelper;
        state.setScreen(GlobalState.Screen.Welcome);

        configurator.configureFromResources();
    }

    @RequestMapping("/state")
    public SessionState state(HttpServletRequest request) {

        return new SessionState() {{
            setAdmin(servletHelper.isAdmin());
            setMyPlayer(servletHelper.getPlayer());
            setGlobalState(state);
        }};
    }
}
