package hoyley.gshow.model.choiceGame;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Wither;

@Builder(toBuilder = true)
@Getter
@Wither
public class GameStatus {

    private int remainingTime;
    private int remainingPoints;
    private boolean isGameOver;
    
}
