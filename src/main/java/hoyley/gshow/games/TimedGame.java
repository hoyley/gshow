package hoyley.gshow.games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimedGame {

    private static final Logger logger = LoggerFactory.getLogger(TimedGame.class);

    private final TimedGameConfig config;
    private final Timer timer = new Timer();
    protected final Runnable onStateChanged;
    private final DecreaseFunction pointReduction;
    private int secondsRemaining;
    private int currentPoints;
    private boolean isGameOver = false;

    public TimedGame(TimedGameConfig config, DecreaseFunction pointReduction, Runnable onStateChanged) {
        this.config = config;
        this.pointReduction = pointReduction;
        this.onStateChanged = onStateChanged;
        secondsRemaining = config.getSeconds();
        currentPoints = config.getStartingPoints();
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public int getSecondsElapsed() {
        return config.getSeconds() - secondsRemaining;
    }
    public int getCurrentPoints() {
        return currentPoints;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void play() {
        timer.scheduleAtFixedRate(new TimerTask() {
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

    protected void onCountDown() {

    }

    public void gameOver() {
        timer.cancel();
        isGameOver = true;
        onStateChanged.run();
    }

    protected double getTimeFraction() {
        return (double) secondsRemaining / config.getSeconds();
    }

    private void countDown() {
        secondsRemaining -= 1;

        currentPoints = pointReduction.compute(config.getStartingPoints(), getTimeFraction());

        // Allow the child class to take action
        onCountDown();

        if (secondsRemaining == 0) {
            gameOver();
        } else {
            onStateChanged.run();
        }
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
