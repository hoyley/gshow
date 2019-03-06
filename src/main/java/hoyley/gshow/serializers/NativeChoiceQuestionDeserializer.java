package hoyley.gshow.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hoyley.gshow.model.choiceGame.ChoiceOption;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NativeChoiceQuestionDeserializer extends ChoiceQuestionDeserializer {

    @Override
    public List<ChoiceQuestion> deserialize(JsonNode input) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<ChoiceQuestion> questions = Arrays.asList(
                mapper.treeToValue(input, ChoiceQuestion[].class));

            return questions;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Could not deserialize ChoiceQuestion input.");
        }
    }
}
