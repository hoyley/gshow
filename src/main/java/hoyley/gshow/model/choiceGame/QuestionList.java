package hoyley.gshow.model.choiceGame;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionList {

    private final ArrayList<ChoiceQuestion> choiceQuestions = new ArrayList<>();
    private int currentQuestionIndex = -1;

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

    public void reset() {
        currentQuestionIndex = -1;
    }

    public boolean isDone() {
        return currentQuestionIndex == choiceQuestions.size() -1;
    }
}
