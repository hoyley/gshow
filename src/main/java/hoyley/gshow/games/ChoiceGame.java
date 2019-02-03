package hoyley.gshow.games;

import hoyley.gshow.model.ChoiceQuestion;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.PlayerAnswer;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChoiceGame extends TimedGame {

    private final ChoiceQuestion question;
    private final ChoiceQuestion originalQuestion;
    private final HashMap<Player, PlayerAnswer> answers = new LinkedHashMap<>();

    public ChoiceGame(ChoiceQuestion question, TimedGameConfig timeConfig, Runnable onStateChanged) {
        super(timeConfig, onStateChanged);

        this.originalQuestion = question;
        this.question = (ChoiceQuestion) question.clone();
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    public ChoiceQuestion getOriginalQuestion() {
        return originalQuestion;
    }

    public HashMap<Player, PlayerAnswer> getAnswers() {
        return answers;
    }

    public void submitAnswer(String answer, Player player) {
        if (answers.containsKey(player)) {
            throw new RuntimeException("Attempted duplicate submission.");
        }

        answers.put(player, new PlayerAnswer() {{
            setId(player.getId());
            setAnswer(answer);
            setTimeElapsed(getSecondsElapsed());
            setPoints(getCurrentPoints());
            setCorrect(question.getAnswer().equals(answer));
        }});
        
        onStateChanged.run();
    }

    @Override
    public void gameOver() {
        super.gameOver();
    }
}
