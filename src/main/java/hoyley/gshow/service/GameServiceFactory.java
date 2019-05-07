package hoyley.gshow.service;

import hoyley.gshow.model.choiceGame.QuestionList;
import hoyley.gshow.model.state.GlobalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class GameServiceFactory {
    private final QuestionList questionList;
    private final int gameOverDelayMillis;
    private final ApplicationEventPublisher publisher;
    private int guessTimeSecs;

    @Autowired
    public GameServiceFactory(QuestionList questionList,
                              ApplicationEventPublisher publisher,
                              @Value("${game.overDelayMillis:5000}") int gameOverDelayMillis,
                              @Value("${game.guessTimeSecs:10}") int guessTimeSecs) {
        this.questionList = questionList;
        this.gameOverDelayMillis = gameOverDelayMillis;
        this.guessTimeSecs = guessTimeSecs;
        this.publisher = publisher;
    }

    public GameService create(GlobalState state) {
        return new GameService(state, questionList, publisher, gameOverDelayMillis, guessTimeSecs);
    }
}
