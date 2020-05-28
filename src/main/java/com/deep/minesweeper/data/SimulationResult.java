package com.deep.minesweeper.data;

public class SimulationResult {
    private final int gamesWon;
    private final int gamesPlayed;
    private final double averageRoundCount;
    private final long time;
    private final int points;

    public SimulationResult(int gamesWon, int gamesPlayed, double averageRoundCount, long time, int points) {
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;
        this.averageRoundCount = averageRoundCount;
        this.time = time;
        this.points = points;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public double getAverageRoundCount() {
        return averageRoundCount;
    }

    public double getAverageTimeInMs() {
        return time / (gamesPlayed * 1000000.0);
    }

    @Override
    public String toString() {
        return "SimulationResult{\n" + "Games won = " + gamesWon + ",\n Games played = " + gamesPlayed
                + ",\n Average no. of rounds = " + averageRoundCount + ",\n Average time = "
                + time / (gamesPlayed * 1000000) + "ms (per game)" + ",\n Average points = " + points + "\n}";
    }
}
