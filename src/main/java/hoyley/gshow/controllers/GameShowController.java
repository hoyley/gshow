package hoyley.gshow.controllers;

import hoyley.gshow.Configurator;
import hoyley.gshow.model.RootState;
import hoyley.gshow.model.screens.WelcomeScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameShowController {

    private final RootState state;
    private final ChoiceGameController choiceGameController;

    @Autowired
    public GameShowController(RootState state, ChoiceGameController choiceGameController,
        Configurator configurator) {
        this.state = state;
        this.choiceGameController = choiceGameController;
        state.setScreen(new WelcomeScreen());

        configurator.configureFromResources();
    }

    @RequestMapping("/state")
    public RootState state() {
        return state;
    }

    @RequestMapping("/startGame")
    public void startGame() {
        chooseNextGame();
    }

    private void chooseNextGame() {
        choiceGameController.startGame();
    }
}
