package hoyley.gshow.controllers;

import hoyley.gshow.Configurator;
import hoyley.gshow.helpers.HttpServletHelper;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import hoyley.gshow.model.screens.WelcomeScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GameShowController {

    private final RootState state;
    private final ChoiceGameController choiceGameController;
    private final HttpServletHelper servletHelper;

    @Autowired
    public GameShowController(RootState state, ChoiceGameController choiceGameController,
                              Configurator configurator, HttpServletHelper servletHelper) {
        this.state = state;
        this.servletHelper = servletHelper;
        this.choiceGameController = choiceGameController;
        state.setScreen(new WelcomeScreen());

        configurator.configureFromResources();
    }

    @RequestMapping("/state")
    public RootState state() {
        return state;
    }

    @RequestMapping("/myPlayer")
    public Player getMyPlayer(HttpServletRequest request) {
        return servletHelper.getPlayer(request);
    }

    @RequestMapping("/startGame")
    public void startGame() {
        chooseNextGame();
    }

    private void chooseNextGame() {
        choiceGameController.startGame();
    }
}
