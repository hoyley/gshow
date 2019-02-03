package hoyley.gshow.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChoiceQuestion implements Cloneable {

    private String question;
    private String answer;
    private List<ChoiceOption> options;

    @Override
    public Object clone() {
        ChoiceQuestion newQuestion = new ChoiceQuestion();
        newQuestion.setAnswer(answer);
        newQuestion.setOptions(new LinkedList<>(options));
        newQuestion.setQuestion(question);
        return newQuestion;
    }

    public void setOptionStrings(List<String> options) {
        setOptions(options.stream()
            .map(o -> new ChoiceOption(o))
            .collect(Collectors.toList()));
    }
}
