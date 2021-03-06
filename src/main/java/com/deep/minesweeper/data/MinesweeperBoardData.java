package com.deep.minesweeper.data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class MinesweeperBoardData {
    private static final int MINE_VALUE = -1;
    private final int columns;
    private final int rows;
    private final int totalMines;
    private final Element[][] board;
    private final int[][] counterBoard;
    private final Set<Position> flagged;
    private final Set<Position> mines;
    private GameState state;

    public MinesweeperBoardData(GameLevel level) {
        this.columns = level.getColumns();
        this.rows = level.getRows();
        this.totalMines = level.getMines();
        this.board = new Element[rows][columns];
        this.counterBoard = new int[rows][columns];
        this.flagged = new HashSet<>();
        this.mines = new HashSet<>();
        this.state = GameState.IN_PLAY;
        initializeBoard();
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public Set<Position> getNeighbors(int row, int column) {
        var neighbors = new HashSet<Position>();
        final int[] rowAdjustments = { -1, 0, 1 };
        final int[] columnAdjustments = { -1, 0, 1 };
        for (var rowAdj : rowAdjustments) {
            for (var columnAdj : columnAdjustments) {
                if (rowAdj == 0 && columnAdj == 0)
                    continue;
                var newRow = row + rowAdj;
                var newColumn = column + columnAdj;
                if (newRow < 0 || newRow >= rows || newColumn < 0 || newColumn >= columns)
                    continue;
                neighbors.add(new Position(newRow, newColumn));
            }
        }
        return neighbors;
    }

    public Set<Position> getNeighbors(Position pos) {
        return getNeighbors(pos.getRow(), pos.getColumn());
    }

    private void initializeBoard() {
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                board[i][j] = Element.COVERED_EMPTY;
                counterBoard[i][j] = 0;
            }
        }
        final int maxCount = rows * columns;
        var generator = new Random();
        for (var i = 0; i < totalMines;) {
            var mineIndex = generator.nextInt(maxCount);
            var mineRow = mineIndex / columns;
            var mineColumn = mineIndex % columns;
            if (board[mineRow][mineColumn] == Element.COVERED_EMPTY) {
                board[mineRow][mineColumn] = Element.COVERED_MINE;
                mines.add(new Position(mineRow, mineColumn));
                ++i;
                var neighbors = getNeighbors(mineRow, mineColumn);
                counterBoard[mineRow][mineColumn] = MINE_VALUE;
                for (var neighbor : neighbors) {
                    if (board[neighbor.getRow()][neighbor.getColumn()] == Element.COVERED_EMPTY) {
                        counterBoard[neighbor.getRow()][neighbor.getColumn()]++;
                    }
                }
            }
        }
    }

    public void resetBoard() {
        mines.clear();
        initializeBoard();
        flagged.clear();
        state = GameState.IN_PLAY;
    }

    public GameState getGameState() {
        return state;
    }

    public int getMineCount(int row, int column) {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw new IllegalArgumentException("Invalid position: Position outside board");
        return counterBoard[row][column];
    }

    public int getMineCount(Position position) {
        return getMineCount(position.getRow(), position.getColumn());
    }

    public Element getMineState(int row, int column) {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw new IllegalArgumentException("Invalid position: Position outside board");
        return board[row][column];
    }

    public void flagCell(int row, int column) {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw new IllegalArgumentException("Invalid position: Position outside board");
        if (board[row][column] == Element.COVERED_EMPTY || board[row][column] == Element.COVERED_MINE) {
            board[row][column] = Element.FLAGGED;
            flagged.add(new Position(row, column));
        }
        if (flagged.equals(mines))
            state = GameState.WON;
    }

    public void flagCell(Position position) {
        flagCell(position.getRow(), position.getColumn());
    }

    public int getFlaggedCount() {
        return flagged.size();
    }

    public boolean isGameEnded() {
        return state != GameState.IN_PLAY;
    }

    public void uncoverCell(int row, int column) {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw new IllegalArgumentException("Invalid position: Position outside board");
        if (board[row][column] == Element.COVERED_EMPTY) {
            board[row][column] = Element.UNCOVERED_EMPTY;
            recursivelyUncover(new Position(row, column), new HashSet<>());
            if (checkIfOnlyMinesCovered())
                state = GameState.WON;
        } else if (board[row][column] == Element.COVERED_MINE) {
            uncoverAllMines();
        }
    }

    public void uncoverCell(Position position) {
        uncoverCell(position.getRow(), position.getColumn());
    }

    private void uncoverAllMines() {
        state = GameState.LOST;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == Element.COVERED_MINE) {
                    board[i][j] = Element.UNCOVERED_MINE;
                }
            }
        }
        markWronglyFlagged();
    }

    private void recursivelyUncover(Position pos, Set<Position> done) {
        if (counterBoard[pos.getRow()][pos.getColumn()] == 0 && !done.contains(pos)) {
            done.add(pos);
            var neighbors = getNeighbors(pos);
            for (var neighbor : neighbors) {
                recursivelyUncover(neighbor, done);
            }
        }
        board[pos.getRow()][pos.getColumn()] = Element.UNCOVERED_EMPTY;
    }

    private boolean checkIfOnlyMinesCovered() {
        var uncovered = getUncovered();
        return uncovered.size() + totalMines == rows * columns;
    }

    public void uncoverNeighbors(int row, int column) {
        var neighbors = getNeighbors(row, column);
        var mineNeighbors = new HashSet<>(neighbors);
        mineNeighbors.retainAll(mines);
        var flaggedNeighbors = new HashSet<>(neighbors);
        flaggedNeighbors.retainAll(flagged);
        Logger.getGlobal().info("Flagged neighbors: " + flaggedNeighbors);
        Logger.getGlobal().info("Mine neighbors: " + mineNeighbors);
        if (mineNeighbors.equals(flaggedNeighbors)) {
            var uncoverNeighbors = new HashSet<>(neighbors);
            uncoverNeighbors.removeAll(flaggedNeighbors);
            for (var cell : uncoverNeighbors) {
                board[cell.getRow()][cell.getColumn()] = Element.UNCOVERED_EMPTY;
            }
        } else if (mineNeighbors.isEmpty()) {
            for (var cell : neighbors) {
                board[cell.getRow()][cell.getColumn()] = Element.UNCOVERED_EMPTY;
            }
        } else if (!mineNeighbors.containsAll(flaggedNeighbors)) {
            uncoverAllMines();
        }
    }

    public Set<Position> getUncovered() {
        var uncovered = new HashSet<Position>();
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                if (board[i][j] == Element.UNCOVERED_EMPTY)
                    uncovered.add(new Position(i, j));
            }
        }
        return uncovered;
    }

    public void uncoverNeighbors(Position cell) {
        uncoverNeighbors(cell.getRow(), cell.getColumn());
    }

    private void markWronglyFlagged() {
        if (!isGameEnded())
            return;
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                if (board[i][j] == Element.FLAGGED && counterBoard[i][j] != -1) {
                    board[i][j] = Element.WRONGLY_FLAGGED;
                    Logger.getGlobal().info("Wrongly flagged: " + i + " " + j);
                    Logger.getGlobal().info("" + board[i][j]);
                }
            }
        }
    }

    public int getPoint() {
        final int UNCOVERED_EMPTY_POINT = 1;
        final int FLAGGED_MINE_POINT = 1000;
        var points = 0;
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                if (board[i][j] == Element.UNCOVERED_EMPTY)
                    points += UNCOVERED_EMPTY_POINT;
                else if (board[i][j] == Element.FLAGGED && counterBoard[i][j] == -1)
                    points += FLAGGED_MINE_POINT;
            }
        }
        return points;
    }

    @Override
    public String toString() {
        var out = new StringBuilder();
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                out.append(String.format("%2d", counterBoard[i][j])).append(" ");
            }
            out.append('\n');
        }
        return out.toString();
    }

    public enum Element {
        COVERED_MINE, COVERED_EMPTY, UNCOVERED_MINE, UNCOVERED_EMPTY, FLAGGED, WRONGLY_FLAGGED
    }

    public enum GameState {
        IN_PLAY, WON, LOST
    }
}
