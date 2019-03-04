package hoyley.gshow.model.choiceGame;

import lombok.Data;

@Data
public class ChoiceOption {
    private String option;
    private boolean eliminated;

    public ChoiceOption(String option) {
        this.option = option;
    }
}
