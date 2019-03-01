package hoyley.gshow.model.ChoiceGame;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionList {

    private final ArrayList<ChoiceQuestion> choiceQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    public void addChoiceQuestion(ChoiceQuestion question) {
        choiceQuestions.add(question);
    }

    public ChoiceQuestion nextQuestion() {
        return getQuestion(currentQuestionIndex + 1);
    }

    public ChoiceQuestion getQuestion(int index) {
        if (index >= choiceQuestions.size() || index < 0) {
            throw new RuntimeException("Attempted to get a question out of bounds. Attempted: " + index);
        }
        this.currentQuestionIndex = index;
        return choiceQuestions.get(currentQuestionIndex);
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getTotalQuestions() {
        return choiceQuestions.size();
    }
}
