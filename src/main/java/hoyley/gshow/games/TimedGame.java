package hoyley.gshow.games;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimedGame {

    private final TimedGameConfig config;
    private final Timer timer = new Timer();
    private final Runnable onStateChanged;
    private int secondsRemaining;
    private int currentPoints;
    private boolean isGameOver = false;

    public TimedGame(TimedGameConfig config, Runnable onStateChanged) {
        this.config = config;
        this.onStateChanged = onStateChanged;
        secondsRemaining = config.getSeconds();
        currentPoints = config.getStartingPoints();
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void startGame() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countDown();
            }
        }, 1000, 1000);
        onStateChanged.run();
    }

    private void countDown() {
        secondsRemaining -= 1;

        int numIntervalsCounted = (config.getSeconds() - secondsRemaining) / config.getIntervalSeconds();
        int pointsLost = numIntervalsCounted * config.getRemovePointsPerInterval();
        currentPoints = Math.max(config.getStartingPoints() - pointsLost, 0);

        if (secondsRemaining == 0) {
            gameOver();
        } else {
            onStateChanged.run();
        }
    }

    public void gameOver() {
        timer.cancel();
        isGameOver = true;
        onStateChanged.run();
    }
}
