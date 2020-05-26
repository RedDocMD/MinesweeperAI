package com.deep.minesweeper.data;

public class GameLevel {
    private final int rows;
    private final int columns;
    private final int mines;
    private final String label;

    public static final GameLevel BEGINNER = new GameLevel(10, 10, 10, "Beginner");
    public static final GameLevel INTERMEDIATE = new GameLevel(16, 16, 40, "intermediate");
    public static final GameLevel EXPERT = new GameLevel(16, 30, 99, "Expert");


    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getMines() {
        return mines;
    }

    public String getLabel() {
        return label;
    }

    public GameLevel(int rows, int columns, int mines, String label) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.label = label;
    }
}
