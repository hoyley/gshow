package hoyley.gshow.model.choiceGame;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChoiceQuestion implements Cloneable {

    private String question;
    private String answer;
    private String imagePath;
    private List<ChoiceOption> options;

    @Override
    public Object clone() {
        ChoiceQuestion newQuestion = new ChoiceQuestion();
        newQuestion.setAnswer(answer);
        newQuestion.setOptionList(options);
        newQuestion.setQuestion(question);
        newQuestion.setImagePath(imagePath);
        return newQuestion;
    }

    public void setOptionStrings(List<String> options) {
        setOptions(options.stream()
            .map(o -> new ChoiceOption(o))
            .collect(Collectors.toList()));
    }

    public void setOptionList(List<ChoiceOption> options) {
        List<String> optionStrings = options.stream().map(o -> o.getOption()).collect(Collectors.toList());
        setOptionStrings(optionStrings);
    }
}
