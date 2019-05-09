package hoyley.gshow.controllers;

import hoyley.gshow.QuestionLoader;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.state.SessionState;
import hoyley.gshow.service.SessionManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GameShowController {

    private final SessionManagementService sessionService;

    public GameShowController(SessionManagementService sessionService, QuestionLoader questionLoader) {
        this.sessionService = sessionService;

        questionLoader.configure();
    }

    @RequestMapping("/state")
    public SessionState state(HttpServletRequest request) {
        PlayerHelper playerHelper = new PlayerHelper(request.getSession().getId(),
            sessionService.getSessionSafe("main").getGame().getState());
        
        return sessionService.getSessionSafe("main").getState().getSessionState(playerHelper);
    }
}
