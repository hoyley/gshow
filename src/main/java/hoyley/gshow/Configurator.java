package hoyley.gshow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hoyley.gshow.model.ChoiceQuestion;
import hoyley.gshow.model.GameState;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
            setAnswer("3");
            setOptionStrings(Arrays.asList("1", "2", "3", "4"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptionStrings(Arrays.asList("1", "2", "3", "4", "5"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptionStrings(Arrays.asList("1", "2", "3", "4", "5", "6"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptionStrings(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        }});

        gameState.addChoiceQuestion(new ChoiceQuestion() {{
            setQuestion("How many Oranges?");
            setAnswer("3");
            setOptionStrings(Arrays.asList("3", "There are two apples", "There are two apples", "There are two apples", "There are two apples", "6", "7", "8", "9", "10"));
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

    public void configureFromResources() {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("ChoiceQuestions.txt");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;

            node = mapper.readTree(stream);
            configureFromJson(node);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading json file.", ex);
        }

    }

    private void configureFromJson(JsonNode node) {
        if (node.isArray() == false) {
            throw new RuntimeException("Expected outer array.");
        }

        List<ChoiceQuestion> questions = new LinkedList<>();

        node.forEach(question -> {
            ChoiceQuestion newQuestion = new ChoiceQuestion();
            newQuestion.setQuestion(HtmlUtils.htmlUnescape(question.get("question").asText()));
            newQuestion.setAnswer(HtmlUtils.htmlUnescape(question.get("correct_answer").asText()));

            List<String> answers = new LinkedList<>();
            for (JsonNode answerNode : question.get("incorrect_answers")) {
                answers.add(HtmlUtils.htmlUnescape(answerNode.asText()));
            }
            answers.add(newQuestion.getAnswer());
            Collections.shuffle(answers);
            newQuestion.setOptionStrings(answers);

            questions.add(newQuestion);
        });

        Collections.shuffle(questions);

        questions.forEach(q -> gameState.addChoiceQuestion(q));
    }
}
