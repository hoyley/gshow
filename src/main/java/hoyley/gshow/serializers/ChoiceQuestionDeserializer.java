package hoyley.gshow.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class ChoiceQuestionDeserializer {

    public List<ChoiceQuestion> deserializeResource(String resourceName) {
        return deserialize(getClass().getClassLoader().getResourceAsStream("ChoiceQuestions.txt"));
    }

    public List<ChoiceQuestion> deserialize(InputStream input) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;

            node = mapper.readTree(input);
            return deserialize(node);
        } catch (IOException ex) {
            throw new RuntimeException("Error deserializing ChoiceQuestions from input stream.", ex);
        }
    }

    public abstract List<ChoiceQuestion> deserialize(JsonNode input);
    
}
