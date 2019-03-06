package hoyley.gshow;

import hoyley.gshow.helpers.QuestionHelper;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import hoyley.gshow.model.choiceGame.QuestionList;
import hoyley.gshow.serializers.ChoiceQuestionDeserializer;
import hoyley.gshow.serializers.NativeChoiceQuestionDeserializer;
import hoyley.gshow.serializers.OpenTriviaDbDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class Configurator {

    public enum QuestionSourceFormat {
        OpenTDB,
        Native
    }

    @Autowired
    private QuestionList questionList;

    @Value("${game.questionSource.format}")
    private QuestionSourceFormat questionSourceFormat;

    @Value("${game.questionSource.file}")
    private Resource questionSourceFile;

    @Value("${game.questions.enforceAnswerFairness}")
    private boolean enforceAnswerFairness;

    @Value("${game.questions.maxOptions:10}")
    private int maxOptions;

    private ChoiceQuestionDeserializer deserializer;


    public void configure() {

        switch (questionSourceFormat) {
            case OpenTDB:
                deserializer = new OpenTriviaDbDeserializer();
                break;
            case Native:
                deserializer = new NativeChoiceQuestionDeserializer();
                break;
            default:
                throw new RuntimeException(String.format("Unrecognized 'game.questionSource.format' [%s].",
                    questionSourceFormat));
        }

        loadQuestions();
    }

    public List<ChoiceQuestion> loadInternalResource() {
        InputStream stream;

        try {
            stream = questionSourceFile.getInputStream();
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Could not load InputStream."));
        }
        return deserializer.deserialize(stream);
    }

    private void loadQuestions() {
        List<ChoiceQuestion> questions = loadInternalResource();
        if (enforceAnswerFairness) {
            questions = QuestionHelper.enforceTargetFairness(questions);
        }
        QuestionHelper.processOptions(questions, maxOptions);
        questions.forEach(q -> questionList.addChoiceQuestion(q));
    }
}
