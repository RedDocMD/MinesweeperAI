package com.deep.minesweeper.data;

public class SimulationResult {
    private final int gamesWon;
    private final int gamesPlayed;
    private final double averageRoundCount;
    private final long time;

    public SimulationResult(int gamesWon, int gamesPlayed, double averageRoundCount, long time) {
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;
        this.averageRoundCount = averageRoundCount;
        this.time = time;
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
        return "SimulationResult{\n" +
                "gamesWon = " + gamesWon +
                ",\n gamesPlayed = " + gamesPlayed +
                ",\n averageRoundCount = " + averageRoundCount +
                ",\n time = " + time / (gamesPlayed * 1000000) + "ms (per round)" +
                "\n}";
    }
}
