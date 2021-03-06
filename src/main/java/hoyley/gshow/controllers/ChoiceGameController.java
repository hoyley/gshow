package hoyley.gshow.controllers;

import hoyley.gshow.Constants;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.service.GameService;
import hoyley.gshow.service.GameRoomManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/game/choice")
public class ChoiceGameController {

    @Autowired
    private GameRoomManagementService gameRoomManagementService;
    @Autowired
    private HttpServletRequest servletRequest;

    @PostMapping
    public void submitAnswer(@RequestBody String answer) {
        gameService().asPlayer(getPlayerHelper()).submitAnswer(answer);
    }

    @RequestMapping("/next")
    public void nextGame() {
        gameService().asAdmin(getPlayerHelper()).nextGame();
    }

    @RequestMapping("/goTo")
    public void goToGame(@RequestParam int num) {
        gameService().asAdmin(getPlayerHelper()).goToGame(num);
    }

    @GetMapping("/end")
    public void endGame() {
        gameService().asAdmin(getPlayerHelper()).endGame();
    }

    @PostMapping("/guessTime")
    public void setGuessTimeSecs(@RequestParam int guessTimeSecs) {
        gameService().asAdmin(getPlayerHelper()).setGuessTimeSecs(guessTimeSecs);
    }

    private GameService gameService() {
        return gameRoomManagementService.getGameRoom(Constants.DEFAULT_GAME_ROOM).getGame();
    }

    private PlayerHelper getPlayerHelper() {
        return new PlayerHelper(servletRequest.getSession().getId(), gameService().getState());
    }
}
