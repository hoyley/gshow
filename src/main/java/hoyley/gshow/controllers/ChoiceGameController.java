package hoyley.gshow.controllers;

import hoyley.gshow.games.ChoiceGame;
import hoyley.gshow.games.TimedGameConfig;
import hoyley.gshow.model.GameState;
import hoyley.gshow.model.RootState;
import hoyley.gshow.model.screens.ChoiceGameScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/choice")
public class ChoiceGameController {

    private final RootState state;
    private final GameState gameState;
    private ChoiceGame choiceGame;

    @Autowired
    public ChoiceGameController(RootState state, GameState gameState) {
        this.state = state;
        this.gameState = gameState;
    }

    public void startGame() {
        choiceGame = new ChoiceGame(gameState.popChoiceQuestion(), TimedGameConfig.evenCountDown(20, 500),
                this::onGameStateChange);
        choiceGame.startGame();
    }

    private void onGameStateChange() {
        ChoiceGameScreen screen = new ChoiceGameScreen();
        screen.setQuestion(choiceGame.getQuestion().getQuestion());
        screen.setOptions(choiceGame.getQuestion().getOptions());
        screen.setRemainingPoints(choiceGame.getCurrentPoints());
        screen.setRemainingTime(choiceGame.getSecondsRemaining());
        screen.setGameOver(choiceGame.isGameOver());

        if (choiceGame.isGameOver()) {
            screen.setAnswer(choiceGame.getQuestion().getAnswer());
        }
        state.setScreen(screen);
    }

}
