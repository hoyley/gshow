package hoyley.gshow.model;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameState {

    private final Stack<ChoiceQuestion> choiceQuestions = new Stack<>();
    private final List<ChoiceQuestion> usedChoiceQuestions = new LinkedList<>();

    public void addChoiceQuestion(ChoiceQuestion question) {
        choiceQuestions.add(question);
    }

    public ChoiceQuestion popChoiceQuestion() {
        if (choiceQuestions.empty()) {
            throw new RuntimeException("Attempted to pop a question, but no questions left.");
        }
        ChoiceQuestion question = choiceQuestions.pop();
        usedChoiceQuestions.add(question);
        return question;
    }

}
