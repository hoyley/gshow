package hoyley.gshow.games;

import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.choiceGame.PlayerAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class ChoiceGame extends TimedGame {

    private final static Logger logger = LoggerFactory.getLogger(ChoiceGame.class);
    private final ChoiceQuestion question;
    private final ChoiceQuestion originalQuestion;
    private final DecreaseFunction optionReduction;
    private final HashMap<Player, PlayerAnswer> answers = new LinkedHashMap<>();
    private final Random random = new Random();

    public ChoiceGame(ChoiceQuestion question, TimedGameConfig timeConfig, DecreaseFunction pointReduction,
                      DecreaseFunction optionReduction, Runnable onStateChanged) {
        super(timeConfig, pointReduction, onStateChanged);

        this.originalQuestion = question;
        this.optionReduction = optionReduction;
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
            logger.debug("Duplicate answer submission detected from player [{}].", player.getNickname());
            return;  // We're going to ignore this silently
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
    protected void onCountDown() {
        super.onCountDown();
        reduceOptions();
    }

    private void reduceOptions() {
        int originalOptions = originalQuestion.getOptions().size();
        int currentOptions = (int)question.getOptions().stream().filter(o -> !o.isEliminated()).count();
        int reducedOptions = Math.max(optionReduction.compute(originalOptions, getTimeFraction()), 1);
        int numOptionsToRemove = Math.max(currentOptions - reducedOptions, 0);

        for (int i = 0; i < numOptionsToRemove; i++) {
            int answerIndex = question.getOptions().stream()
                .map(o -> o.getOption()).collect(Collectors.toList())
                .indexOf(question.getAnswer());
            
            int indexToRemove;
            do
                indexToRemove = random.nextInt(question.getOptions().size());
            while (indexToRemove == answerIndex || question.getOptions().get(indexToRemove).isEliminated());
            
            question.getOptions().get(indexToRemove).setEliminated(true);
        }
    }

    @Override
    public boolean isGameOver() {
        return super.isGameOver();
        
    }
}
