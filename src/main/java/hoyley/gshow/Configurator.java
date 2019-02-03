package hoyley.gshow;

import hoyley.gshow.model.ChoiceQuestion;
import hoyley.gshow.model.GameState;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Configurator {

    private final GameState gameState;
    private final RootState rootState;

    @Autowired
    public Configurator(GameState gameState, RootState rootState) {
        this.gameState = gameState;
        this.rootState = rootState;
    }

    public void configure() {
        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many apples?");
            setAnswer("4");
            setOptions(Arrays.asList("1", "2", "3", "4"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptions(Arrays.asList("1", "2", "3", "4", "5"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptions(Arrays.asList("1", "2", "3", "4", "5", "6"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptions(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptions(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        }});
        
        rootState.getRegisteredPlayers().add(new Player() {{
            setId("lskadjfas");
            setNickname("Default Player");
            setSessionId("123");
        }});

        rootState.getRegisteredPlayers().add(new Player() {{
            setId("hsdfga");
            setNickname("Default Player 2");
            setSessionId("125");
        }});
    }
}
