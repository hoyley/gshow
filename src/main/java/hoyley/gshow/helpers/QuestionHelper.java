package hoyley.gshow.helpers;

import hoyley.gshow.model.choiceGame.ChoiceOption;
import hoyley.gshow.model.choiceGame.ChoiceQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QuestionHelper {

    private static final Logger logger = LoggerFactory.getLogger(QuestionHelper.class);
    // The seed we use will determine the shuffle order in a consistent way for future executions
    private static final Random r = new Random(4);

    public static List<ChoiceQuestion> enforceTargetFairness(List<ChoiceQuestion> questions) {
        HashMap<String, List<ChoiceQuestion>> questionMap = new HashMap<>();
        int maxColumns = 0;

        // Index all questions by target ID
        for (ChoiceQuestion q : questions) {
            List<ChoiceQuestion> qList = questionMap.getOrDefault(q.getTargetIdOrAnswer(),
                new LinkedList<>());
            if (qList.size() == 0) {
                questionMap.put(q.getTargetIdOrAnswer(), qList);
            }
            qList.add(q);
            if (maxColumns < qList.size()) {
                maxColumns = qList.size();
            }
        };

        // Shuffle all lists of questions by target ID
        questionMap.values().forEach(qs -> Collections.shuffle(qs, r));

        logger.info("Enforcing fairness for {} questions: {}", questions.size(), getLogString(questionMap));

        List<ChoiceQuestion> shuffledQuestions = new LinkedList<>();
        for (int i = 0; i < maxColumns; i++) {
            List<ChoiceQuestion> questionsWithUniqueTarget = getColumn(questionMap, i);
            Collections.shuffle(questionsWithUniqueTarget, r);
            shuffledQuestions.addAll(questionsWithUniqueTarget);
        }
        
        return shuffledQuestions;
    }

    /**
     * This method ensures that the answer exists in the list of options, shuffles the options,
     * and caps options to maxOptions
     * @param questions
     * @param maxOptions
     */
    public static void processOptions(List<ChoiceQuestion> questions, int maxOptions) {
        questions.forEach(q -> {
            List<ChoiceOption> options = q.getOptions();

            // Remove the answer to make pruning the list easier, and to account for the fact
            // that the answer may not be in the options (we'll add it later)
            options.removeIf(o -> o.getOption().equals(q.getAnswer()));

            // Prune the list to the max size (minus 1 for the answer)
            if (options.size() >= maxOptions) {
                Collections.shuffle(options);
                options = options.subList(0, maxOptions - 1);
            }

            // Add the answer back
            options.add(new ChoiceOption(q.getAnswer()));

            Collections.shuffle(options);
            q.setOptions(options);
        });
        Collections.shuffle(questions, r);
    }

    private static <T> List<T> getColumn(Map<?, List<T>> map, int columnNum) {
        List<T> column = new LinkedList<>();
        for (List<T> val : map.values()) {
            if (val.size() > columnNum) {
                column.add(val.get(columnNum));
            }
        }
        return column;
    }

    private static String getLogString(HashMap<String, List<ChoiceQuestion>> questionMap) {
        StringBuilder builder = new StringBuilder();
        String prepend = "";

        for (String k : questionMap.keySet()) {
            builder
                .append(prepend)
                .append("'")
                .append(k)
                .append("' (")
                .append(questionMap.get(k).size())
                .append(")");

            prepend = ", ";
        };

        return builder.toString();
    }

}
