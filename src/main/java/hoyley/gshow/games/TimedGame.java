package hoyley.gshow.games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public abstract class TimedGame {

    private static final Logger logger = LoggerFactory.getLogger(TimedGame.class);

    private final TimedGameConfig config;
    private final Timer timer = new Timer("ChoiceGameTimer", true);
    protected final Runnable onStateChanged;
    private final DecreaseFunction pointReduction;
    private int secondsDuration;
    private LocalDateTime endGameTime;
    private LocalDateTime startGameTime;
    private boolean isGameOver = false;

    public TimedGame(TimedGameConfig config, DecreaseFunction pointReduction, Runnable onStateChanged) {
        this.config = config;
        this.pointReduction = pointReduction;
        this.onStateChanged = onStateChanged;
        secondsDuration = config.getSeconds();
    }

    public int getSecondsRemaining() {
        return (int)Math.max(0, LocalDateTime.now().until(endGameTime, ChronoUnit.SECONDS));
    }

    public int getSecondsElapsed() {
        return config.getSeconds() - getSecondsRemaining();
    }

    public int getCurrentPoints() {
        return pointReduction.compute(config.getStartingPoints(), getTimeFraction());
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void play() {
        startGameTime = LocalDateTime.now();
        endGameTime = startGameTime.plusSeconds(secondsDuration);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    gameOver();
                } catch (Exception ex) {
                    logger.error("Error in background timer.", ex);
                }
            }
        }, secondsDuration * 1000L);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    countDown();
                } catch (Exception ex) {
                    logger.error("Error in background timer.", ex);
                }
            }
        }, 1000, 1000);

        onStateChanged.run();
    }

    public void pause() {
        timer.cancel();
    }

    private void countDown() {
        onCountDown();
        if (onStateChanged != null) {
            this.onStateChanged.run();
        }
    }
    protected void onCountDown() {

    }

    public void gameOver() {
        timer.cancel();
        isGameOver = true;
        onStateChanged.run();
    }

    protected double getTimeFraction() {
        return (double) getSecondsRemaining() / config.getSeconds();
    }

    public interface DecreaseFunction {
        int compute(int starting, double timeFraction);

        static int constant(int starting, double timeFraction) {
            return starting;
        }

        static int even(int starting, double timeFraction) {
            return (int)(timeFraction * starting);
        }

        static DecreaseFunction evenByPercentage(double percent) {
            return (starting, timeFraction) -> {
                double timeFractionsRemoved = Math.floor((1 - timeFraction) / percent);
                return (int)(starting - (timeFractionsRemoved * percent * starting));
            };
        }
    }
}
