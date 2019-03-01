package hoyley.gshow.model.ChoiceGame;

import lombok.Data;

@Data
public class PlayerAnswer {
    private String id;
    private String answer;
    private int timeElapsed;
    private int points;
    private boolean isCorrect;

    public PlayerAnswer cloneSecret() {
        return new PlayerAnswer() {{
            setId(id);
            setTimeElapsed(timeElapsed);
            setPoints(points);
        }};
    }
}
