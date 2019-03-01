package hoyley.gshow.model.state;

import hoyley.gshow.model.ChoiceGame.ChoiceOption;
import hoyley.gshow.model.ChoiceGame.GameStatus;
import hoyley.gshow.model.ChoiceGame.PlayerAnswer;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class ChoiceGameState {

    private String question;
    private List<ChoiceOption> options;
    private Collection<PlayerAnswer> playerAnswers;
    private String answer;
    private GameStatus status;
    private int currentQuestion;
    private int totalQuestions;
    
    public ChoiceGameState() {
        status = new GameStatus();
    }

}
