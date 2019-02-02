package hoyley.gshow.games;

import hoyley.gshow.model.ChoiceQuestion;

public class ChoiceGame extends TimedGame {

    private final ChoiceQuestion question;
    private final ChoiceQuestion originalQuestion;

    public ChoiceGame(ChoiceQuestion question, TimedGameConfig timeConfig, Runnable onStateChanged) {
        super(timeConfig, onStateChanged);

        this.originalQuestion = question;
        this.question = (ChoiceQuestion) question.clone();
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    public ChoiceQuestion getOriginalQuestion() {
        return originalQuestion;
    }

    @Override
    public void gameOver() {
        super.gameOver();
    }
}
