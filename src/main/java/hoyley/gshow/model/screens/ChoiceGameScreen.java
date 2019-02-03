package hoyley.gshow.model.screens;

import hoyley.gshow.model.ChoiceOption;
import hoyley.gshow.model.GameStatus;
import hoyley.gshow.model.PlayerAnswer;
import hoyley.gshow.model.Screen;
import lombok.Data;

import java.util.*;

@Data
public class ChoiceGameScreen extends Screen {

    private String question;
    private List<ChoiceOption> options;
    private Collection<PlayerAnswer> playerAnswers;
    private String answer;
    private GameStatus status;
    
    public ChoiceGameScreen() {
        setName("ChoiceGame");
        status = new GameStatus();
    }

}
