package hoyley.gshow.model.ChoiceGame;

import lombok.Data;

@Data
public class GameStatus {

    private int remainingTime;
    private int remainingPoints;
    private boolean isGameOver = false;
    
}
