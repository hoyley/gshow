package hoyley.gshow.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import org.springframework.web.util.HtmlUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OpenTriviaDbDeserializer extends ChoiceQuestionDeserializer {
    @Override
    public List<ChoiceQuestion> deserialize(JsonNode node) {

        if (node.isArray() == false) {
            throw new RuntimeException("Expected outer array.");
        }

        List<ChoiceQuestion> questions = new LinkedList<>();

        node.forEach(question -> {
            ChoiceQuestion newQuestion = new ChoiceQuestion();
            JsonNode imagePath = question.get("image_path");
            newQuestion.setQuestion(HtmlUtils.htmlUnescape(question.get("question").asText()));
            newQuestion.setAnswer(HtmlUtils.htmlUnescape(question.get("correct_answer").asText()));

            if (imagePath != null) {
                newQuestion.setImagePath(HtmlUtils.htmlUnescape(imagePath.asText()));
            }

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

        return questions;
    }
}
