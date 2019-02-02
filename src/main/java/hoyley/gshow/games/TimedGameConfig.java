package hoyley.gshow.games;

import lombok.Data;

@Data()
public class TimedGameConfig {
    private int seconds;
    private int startingPoints;
    private int removePointsPerInterval;
    private int intervalSeconds;

    public TimedGameConfig(int seconds, int startingPoints, int removePointsPerInterval, int intervalSeconds) {
        this.seconds = seconds;
        this.startingPoints = startingPoints;
        this.removePointsPerInterval = removePointsPerInterval;
        this.intervalSeconds = intervalSeconds;
    }

    public static TimedGameConfig evenCountDown(int seconds, int startingPoints) {
        return new TimedGameConfig(seconds, startingPoints, startingPoints / seconds, 1);
    }
}
