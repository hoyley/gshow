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

    private static final int MAX_OPTIONS = 10;
    
    @Override
    public List<ChoiceQuestion> deserialize(JsonNode input) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<ChoiceQuestion> questions = Arrays.asList(
                mapper.treeToValue(input, ChoiceQuestion[].class));

            postProcessing(questions);
            return questions;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Could not deserialize ChoiceQuestion input.");
        }
    }

    private void postProcessing(List<ChoiceQuestion> questions) {
        questions.forEach(q -> {
            List<ChoiceOption> options = q.getOptions();

            // Remove the answer to make pruning the list easier, and to account for the fact
            // that the answer may not be in the options (we'll add it later)
            options.removeIf(o -> o.getOption().equals(q.getAnswer()));

            // Prune the list to the max size (minus 1 for the answer)
            if (options.size() >= MAX_OPTIONS) {
                Collections.shuffle(options);
                options = options.subList(0, MAX_OPTIONS - 1);
            }

            // Add the answer back
            options.add(new ChoiceOption(q.getAnswer()));
            
            Collections.shuffle(options);
            q.setOptions(options);
        });
        Collections.shuffle(questions);
    }
}
