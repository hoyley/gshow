package hoyley.gshow;

import hoyley.gshow.model.ChoiceQuestion;
import hoyley.gshow.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Configurator {

    private final GameState state;

    @Autowired
    public Configurator(GameState state) {
        this.state = state;
    }

    public void configure() {
        state.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many apples?");
            setAnswer("4");
            setOptions(Arrays.asList("1", "2", "3", "4"));
        }});

        state.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptions(Arrays.asList("1", "2", "3", "4"));
        }});
    }
}
