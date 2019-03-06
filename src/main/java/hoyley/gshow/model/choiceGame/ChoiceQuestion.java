package hoyley.gshow.model.choiceGame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChoiceQuestion implements Cloneable {

    private String question;
    private String answer;
    private String imagePath;
    private List<ChoiceOption> options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private double timeMultiplier = 1;
    
    // This property is useful for the `game.questions.enforceAnswerFairness` application configuration
    // value. Target ID will refer to the question's subject or target (the person it's about) if the
    // answer doesn't do so.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String targetId;

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

    public String getTargetIdOrAnswer() {
        if (this.targetId != null) {
            return targetId;
        } else {
            return answer;
        }
    }
}
