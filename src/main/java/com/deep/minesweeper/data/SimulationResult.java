package com.deep.minesweeper.data;

public class SimulationResult {
    private final int gamesWon;
    private final int gamesPlayed;
    private final double averageRoundCount;

    public SimulationResult(int gamesWon, int gamesPlayed, double averageRoundCount) {
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;
        this.averageRoundCount = averageRoundCount;
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
                "gamesWon=" + gamesWon +
                ", gamesPlayed=" + gamesPlayed +
                ", averageRoundCount=" + averageRoundCount +
                '}';
    }
}
