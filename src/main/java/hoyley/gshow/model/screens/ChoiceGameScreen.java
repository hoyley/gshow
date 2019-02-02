package hoyley.gshow.model.screens;

import hoyley.gshow.model.Screen;
import lombok.Data;

import java.util.List;

@Data
public class ChoiceGameScreen extends Screen {

    private String question;
    private List<String> options;
    private String answer;
    private int remainingTime;
    private int remainingPoints;
    private boolean isGameOver = false;

    public ChoiceGameScreen() {
        setName("ChoiceGame");
    }

}
