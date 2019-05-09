package hoyley.gshow.service;

import hoyley.gshow.games.ChoiceGame;
import hoyley.gshow.games.TimedGame;
import hoyley.gshow.games.TimedGameConfig;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import hoyley.gshow.model.choiceGame.PlayerAnswer;
import hoyley.gshow.model.choiceGame.QuestionList;
import hoyley.gshow.model.state.ChoiceGameState;
import hoyley.gshow.model.state.GlobalState;
import hoyley.gshow.model.state.StateFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.Timer;
import java.util.stream.Collectors;

public class GameService {

    private final StateFacade state;
    private final QuestionList questionList;
    private final int gameOverDelayMillis;
    private final ApplicationEventPublisher publisher;
    private int guessTimeSecs;
    private ChoiceGame choiceGame;

    public GameService(StateFacade state,
                       QuestionList questionList,
                       ApplicationEventPublisher publisher,
                       @Value("${game.overDelayMillis:5000}") int gameOverDelayMillis,
                       @Value("${game.guessTimeSecs:10}") int guessTimeSecs) {
        this.state = state;
        this.questionList = questionList;
        this.gameOverDelayMillis = gameOverDelayMillis;
        this.guessTimeSecs = guessTimeSecs;
        this.publisher = publisher;
    }

    public StateFacade getState() {
        return state;
    }

    public PlayerControl asPlayer(String playerId) {
        return asPlayer(getPlayerHelper(playerId));
    }

    public PlayerControl asPlayer(PlayerHelper playerHelper) {
        return new PlayerControl(playerHelper, this);
    }

    public AdminControl asAdmin(String playerId) {
        return asAdmin(getPlayerHelper(playerId));
    }

    public AdminControl asAdmin(PlayerHelper playerHelper) {
        if (playerHelper.hasGameControl() == false) {
            throw new RuntimeException(String.format("Player [%s] attempted to control the game but has no control.",
                playerHelper.getPlayerSessionId()));
        }
        return new AdminControl(playerHelper, this);
    }

    public static class AdminControl {
        private final PlayerHelper playerHelper;
        private final GameService gameService;

        public AdminControl(PlayerHelper playerHelper, GameService gameService) {
            this.playerHelper = playerHelper;
            this.gameService = gameService;
        }

        public void nextGame() {
            if (playerHelper.hasGameControl()) {
                gameService.startGame();
            }
        }
        public void goToGame(int num) {
            if (playerHelper.hasGameControl()) {
                gameService.startGame(num);
            }
        }

        public void endGame() {
            if (playerHelper.hasGameControl()) {
                gameService.killGame();
            }
        }

        public void setGuessTimeSecs(int guessTimeSecs) {
            if (playerHelper.hasGameControl()) {
                gameService.guessTimeSecs = guessTimeSecs;
            }
        }
    }

    public static class PlayerControl {
        private final PlayerHelper playerHelper;
        private final GameService gameService;

        public PlayerControl(PlayerHelper playerHelper, GameService gameService) {
            this.playerHelper = playerHelper;
            this.gameService = gameService;
        }

        public void submitAnswer(String answer) {
            if (gameService.choiceGame == null) {
                throw new RuntimeException("Submitted answer but no game in progress");
            }

            Player player = playerHelper.getPlayer();

            if (player == null) {
                throw new RuntimeException("Player does not exist for this session.");
            }

            gameService.choiceGame.submitAnswer(answer, player);
        }
    }

    private void startGame() {
        // If the game is over, go back to the welcome screen
        if (questionList.isDone()) {
            killGame();
            return;
        }
        startGame(questionList.nextQuestion());

    }

    private void startGame(int questionIndex) {
        startGame(questionList.getQuestion(questionIndex));
    }

    private void startGame(ChoiceQuestion question) {
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

        state.batch(s -> {
            s.setChoiceGameState(screen);
            s.setScreen(GlobalState.Screen.ChoiceGame);
        });
    }

    private void initiateGameComplete(ChoiceGameState screen) {
        screen.setAnswer(choiceGame.getQuestion().getAnswer());
        screen.setPlayerAnswers(getPlayerAnswers());
        tallyPoints();
        endGameConditionally();
    }

    private void endGameConditionally() {
        // Only trigger the delay if the admin is not logged in. Otherwise admin controls.
        if (state.isAdminActive() == false) {
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
            .forEach(answer ->
                state.incrementPlayerScore(answer.getId(), answer.getPoints())
            );
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
                 
        Collection<PlayerAnswer> nonAnswers = state.getPlayerIds().stream()
            .filter(p -> !playersThatAnswered.contains(p))
            .map(p -> new PlayerAnswer() {{
                setId(p);
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

    private PlayerHelper getPlayerHelper(String sessionId) {
        return new PlayerHelper(sessionId, getState());
    }
}
