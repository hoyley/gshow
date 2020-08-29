package hoyley.gshow.controllers;

import hoyley.gshow.Constants;
import hoyley.gshow.QuestionLoader;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.state.SessionState;
import hoyley.gshow.service.GameRoomManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GameShowController {

    private final GameRoomManagementService sessionService;

    public GameShowController(GameRoomManagementService sessionService, QuestionLoader questionLoader) {
        this.sessionService = sessionService;

        questionLoader.configure();
    }

    @RequestMapping("/state")
    public SessionState state(HttpServletRequest request) {
        PlayerHelper playerHelper = new PlayerHelper(request.getSession().getId(),
            sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).getGame().getState());
        
        return sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).getState().getSessionState(playerHelper);
    }
}
