package hoyley.gshow.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ChoiceQuestion implements Cloneable {

    private String question;
    private String answer;
    private List<String> options;

    @Override
    public Object clone() {
        ChoiceQuestion newQuestion = new ChoiceQuestion();
        newQuestion.setAnswer(answer);
        newQuestion.setOptions(new LinkedList<>(options));
        newQuestion.setQuestion(question);
        return newQuestion;
    }
}
