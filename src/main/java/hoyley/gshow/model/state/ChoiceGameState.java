package hoyley.gshow.model.state;

import hoyley.gshow.model.choiceGame.ChoiceOption;
import hoyley.gshow.model.choiceGame.GameStatus;
import hoyley.gshow.model.choiceGame.PlayerAnswer;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Wither;

import java.util.Collection;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Wither
public class ChoiceGameState {

    private String question;
    private String imagePath;
    private List<ChoiceOption> options;
    private Collection<PlayerAnswer> playerAnswers;
    private String answer;
    private GameStatus status;
    private int currentQuestion;
    private int totalQuestions;
}
