package hoyley.gshow.controllers;

import hoyley.gshow.games.ChoiceGame;
import hoyley.gshow.games.TimedGame;
import hoyley.gshow.games.TimedGameConfig;
import hoyley.gshow.helpers.HttpRequestHelper;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import hoyley.gshow.model.choiceGame.PlayerAnswer;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.choiceGame.QuestionList;
import hoyley.gshow.model.state.ChoiceGameState;
import hoyley.gshow.model.state.GlobalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Timer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game/choice")
public class ChoiceGameController {

    private final GlobalState state;
    private final QuestionList questionList;
    private final HttpRequestHelper servletHelper;
    private final int gameOverDelayMillis;
    private int guessTimeSecs;
    private ChoiceGame choiceGame;

    @Autowired
    public ChoiceGameController(GlobalState state, QuestionList questionList, HttpRequestHelper servletHelper,
                                @Value("${game.overDelayMillis:5000}") int gameOverDelayMillis,
                                @Value("${game.guessTimeSecs:10}") int guessTimeSecs) {
        this.state = state;
        this.questionList = questionList;
        this.servletHelper = servletHelper;
        this.gameOverDelayMillis = gameOverDelayMillis;
        this.guessTimeSecs = guessTimeSecs;
    }

    @PostMapping
    public void submitAnswer(@RequestBody String answer) {
        if (choiceGame == null) {
            throw new RuntimeException("Submitted answer but no game in progress");
        }

        Player player = servletHelper.getPlayer();

        if (player == null) {
            throw new RuntimeException("Player does not exist for this session.");
        }

        choiceGame.submitAnswer(answer, player);
    }


    @RequestMapping("/next")
    public void nextGame() {
        if (servletHelper.hasGameControl()) {
            startGame();
        }
    }

    @RequestMapping("/goTo")
    public void goToGame(@RequestParam int num) {
        if (servletHelper.hasGameControl()) {
            startGame(num);
        }
    }

    @GetMapping("/end")
    public void endGame() {
        if (servletHelper.hasGameControl()) {
            killGame();
        }
    }

    @PostMapping("/guessTime")
    public void setGuessTimeSecs(@RequestParam int guessTimeSecs) {
        if (servletHelper.hasGameControl()) {
            this.guessTimeSecs = guessTimeSecs;
        }
    }

    public void startGame() {
        // If the game is over, go back to the welcome screen
        if (questionList.isDone()) {
            killGame();
            return;
        }
        startGame(questionList.nextQuestion());

    }

    public void startGame(int questionIndex) {
        startGame(questionList.getQuestion(questionIndex));
    }

    public void startGame(ChoiceQuestion question) {
        killGame();

        int guessTimeSecsAdjusted = (int)(guessTimeSecs * question.getTimeMultiplier());

        choiceGame = new ChoiceGame(question,
            TimedGameConfig.evenCountDown(guessTimeSecsAdjusted, 500),
            TimedGame.DecreaseFunction::even,
            TimedGame.DecreaseFunction.evenByPercentage(0.5),
            this::onGameStateChange);
        choiceGame.play();
    }

    private void onGameStateChange() {
        if (choiceGame == null) return;
        
        ChoiceGameState screen = new ChoiceGameState();
        screen.setQuestion(choiceGame.getQuestion().getQuestion());
        screen.setImagePath(choiceGame.getQuestion().getImagePath());
        screen.setOptions(choiceGame.getQuestion().getOptions());
        screen.setCurrentQuestion(questionList.getCurrentQuestionIndex());
        screen.setTotalQuestions(questionList.getTotalQuestions());
        screen.getStatus().setRemainingPoints(choiceGame.getCurrentPoints());
        screen.getStatus().setRemainingTime(choiceGame.getSecondsRemaining());
        screen.getStatus().setGameOver(choiceGame.isGameOver());

        if (choiceGame.isGameOver()) {
            initiateGameComplete(screen);
        } else {
            screen.setPlayerAnswers(choiceGame.getAnswers().values().stream()
                .map(p -> p.cloneSecret())
                .collect(Collectors.toList()));
        }
        state.setChoiceGameState(screen);
        state.setScreen(GlobalState.Screen.ChoiceGame);
    }

    private void initiateGameComplete(ChoiceGameState screen) {
        screen.setAnswer(choiceGame.getQuestion().getAnswer());
        screen.setPlayerAnswers(getPlayerAnswers());
        tallyPoints();
        endGameConditionally();
    }

    private void endGameConditionally() {
        // Only trigger the delay if the admin is not logged in. Otherwise admin controls.
        if (state.adminIsActive() == false) {
            new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        killGame();
                    }
                },
                gameOverDelayMillis);
        }
    }

    private void tallyPoints() {
        choiceGame.getAnswers().values().stream()
            .filter(answer -> answer.isCorrect())
            .forEach(answer -> {
                state.getRegisteredPlayers().stream()
                    .filter(player -> player.getId().equals(answer.getId()))
                    .forEach(player -> {
                        player.setScore(player.getScore() + answer.getPoints());
                    });
            });
    }

    private void killGame() {
        ChoiceGame choiceGameReference = choiceGame;
        if (choiceGameReference != null) {
            choiceGame = null;
            choiceGameReference.gameOver();
            state.setScreen(GlobalState.Screen.Welcome);
        }
    }

    private Collection<PlayerAnswer> getPlayerAnswers() {
        Collection<PlayerAnswer> answers = choiceGame.getAnswers().values().stream().collect(Collectors.toList());
        Collection<String> playersThatAnswered = answers.stream()
            .map(a -> a.getId())
            .collect(Collectors.toList());

        Collection<PlayerAnswer> nonAnswers = state.getRegisteredPlayers().stream()
            .filter(p -> !playersThatAnswered.contains(p.getId()))
            .map(p -> new PlayerAnswer() {{
                setId(p.getId());
                setPoints(0);
                setCorrect(false);
            }}).collect(Collectors.toList());

        answers.addAll(nonAnswers);

        answers.forEach(a -> {
            if (!a.isCorrect()) {
                a.setPoints(0);
            }
        });
        return answers;
    }

}
