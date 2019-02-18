package hoyley.gshow.controllers;

import hoyley.gshow.games.ChoiceGame;
import hoyley.gshow.games.TimedGame;
import hoyley.gshow.games.TimedGameConfig;
import hoyley.gshow.helpers.HttpServletHelper;
import hoyley.gshow.model.GameState;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.PlayerAnswer;
import hoyley.gshow.model.RootState;
import hoyley.gshow.model.screens.ChoiceGameScreen;
import hoyley.gshow.model.screens.WelcomeScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Timer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game/choice")
public class ChoiceGameController {

    private static final int GAME_OVER_DELAY_MILLIS = 5000;
    private final RootState state;
    private final GameState gameState;
    private final HttpServletHelper servletHelper;
    private ChoiceGame choiceGame;

    @Autowired
    public ChoiceGameController(RootState state, GameState gameState, HttpServletHelper servletHelper) {
        this.state = state;
        this.gameState = gameState;
        this.servletHelper = servletHelper;
    }

    @PostMapping
    public void submitAnswer(@RequestBody String answer, HttpServletRequest request) {
        if (choiceGame == null) {
            throw new RuntimeException("Submitted answer but no game in progress");
        }

        Player player = servletHelper.getPlayer(request);

        if (player == null) {
            throw new RuntimeException("Player does not exist for this session.");
        }

        choiceGame.submitAnswer(answer, player);
    }

    public void startGame() {
        choiceGame = new ChoiceGame(gameState.popChoiceQuestion(),
            TimedGameConfig.evenCountDown(20, 500),
            TimedGame.DecreaseFunction::even,
            TimedGame.DecreaseFunction.evenByPercentage(0.5),
            this::onGameStateChange);
        choiceGame.startGame();
    }

    private void onGameStateChange() {
        ChoiceGameScreen screen = new ChoiceGameScreen();
        screen.setQuestion(choiceGame.getQuestion().getQuestion());
        screen.setOptions(choiceGame.getQuestion().getOptions());
        screen.getStatus().setRemainingPoints(choiceGame.getCurrentPoints());
        screen.getStatus().setRemainingTime(choiceGame.getSecondsRemaining());
        screen.getStatus().setGameOver(choiceGame.isGameOver());

        if (choiceGame.isGameOver()) {
            screen.setAnswer(choiceGame.getQuestion().getAnswer());
            screen.setPlayerAnswers(choiceGame.getAnswers().values());
            endGame(GAME_OVER_DELAY_MILLIS);
        } else {
            screen.setPlayerAnswers(choiceGame.getAnswers().values().stream()
                .map(p -> p.cloneSecret())
                .collect(Collectors.toList()));
        }
        state.setScreen(screen);
    }

    private void endGame(int milliDelay) {
        new Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    gameComplete();
                }
            },
            milliDelay);
    }

    private void gameComplete() {
        choiceGame.getAnswers().values().stream()
            .filter(answer -> answer.isCorrect())
            .forEach(answer -> {
                state.getRegisteredPlayers().stream()
                    .filter(player -> player.getId().equals(answer.getId()))
                    .forEach(player -> {
                        player.setScore(player.getScore() + answer.getPoints());
                    });
            });

        state.setScreen(new WelcomeScreen());

        choiceGame = null;
    }



}
