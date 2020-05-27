package com.deep.minesweeper.ai;

import com.deep.minesweeper.data.MinesweeperBoardData;
import com.deep.minesweeper.data.Position;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MinesweeperAI {
    private static final int INFERENCE_CYCLE_LIMIT = 5;
    private final MinesweeperBoardData board;
    private final List<Sentence> sentences;
    private final Set<Position> knownMines;
    private final Set<Position> knownSafe;
    private final Set<Position> uncovered;
    private final Set<Position> allCells;
    private final Random generator;
    private final double[][] probabilities;

    public MinesweeperAI(MinesweeperBoardData board) {
        this.board = board;
        this.sentences = new ArrayList<>();
        this.knownMines = new HashSet<>();
        this.knownSafe = new HashSet<>();
        this.generator = new Random();
        this.uncovered = new HashSet<>();
        this.allCells = new HashSet<>();
        this.probabilities = new double[board.getRows()][board.getColumns()];
        for (var i = 0; i < board.getRows(); i++) {
            for (var j = 0; j < board.getColumns(); j++) {
                probabilities[i][j] = 0.0;
                allCells.add(new Position(i, j));
            }
        }
    }

    public void reset() {
        sentences.clear();
        knownMines.clear();
        knownSafe.clear();
        uncovered.clear();
        generator.setSeed(System.nanoTime());
    }

    public void makeMove() {
        var move = getNextMove();
        Logger.getGlobal().info("Move: " + move);
        if (move != null) {
            board.uncoverCell(move);
            for (var i = 0; i < INFERENCE_CYCLE_LIMIT; i++) {
                inferenceCycle();
            }
            var modifiedUncovered = board.getUncovered();
            uncovered.addAll(modifiedUncovered);
            flagAllMines();
            updateProbabilities();
        }
        Logger.getGlobal().info(String.format("Uncovered size: %d Mine count: %d Safe count: %d Sentences size: %d",
                uncovered.size(), knownMines.size(), knownSafe.size(), sentences.size()));
    }

    private void inferenceCycle() {
        updateData();
        updateSentences();
        doMutualInference();
        updateSentences();
    }

    private void updateData() {
        var modifiedUncovered = board.getUncovered();
        knownSafe.addAll(modifiedUncovered);

        var uncoveredThisMove = modifiedUncovered.stream().filter(c -> !uncovered.contains(c))
                .collect(Collectors.toSet());

        for (var move : uncoveredThisMove) {
            var neighbours = board.getNeighbours(move);
            var safeNeighbours = neighbours.stream().filter(knownSafe::contains).collect(Collectors.toSet());
            var mineNeighbours = neighbours.stream().filter(knownMines::contains).collect(Collectors.toSet());

            var newSentence = new Sentence(neighbours, board.getMineCount(move), safeNeighbours, mineNeighbours);
            sentences.add(newSentence);
            doSelfInference(newSentence);
        }
    }

    private void doSelfInference(Sentence newSentence) {
        newSentence.markSafes(knownSafe);
        newSentence.markMines(knownMines);
        newSentence.doSelfInference();
        knownSafe.addAll(newSentence.getKnownSafe());
        knownMines.addAll(newSentence.getKnownMine());
    }

    private void doMutualInference() {
        var newSentences = new HashSet<Sentence>();
        for (var sentenceA : sentences) {
            for (var sentenceB : sentences) {
                if (!sentenceA.equals(sentenceB)) {
                    var newSentence = sentenceA.doMutualInference(sentenceB);
                    if (newSentence != null) {
                        newSentence.doSelfInference();
                        newSentences.add(newSentence);
                        knownMines.addAll(newSentence.getKnownMine());
                        knownSafe.addAll(newSentence.getKnownSafe());
                    }
                }
            }
        }
        sentences.addAll(newSentences);
    }

    private void flagAllMines() {
        for (var mine : knownMines) {
            board.flagCell(mine);
        }
    }

    private void updateSentences() {
        for (var sentence : sentences) {
            sentence.markMines(knownMines);
            sentence.markSafes(knownSafe);
        }
    }

    private void updateProbabilities() {
        for (var cell : knownSafe) {
            probabilities[cell.getRow()][cell.getColumn()] = 0.0;
            var unsureNeighbours = board.getNeighbours(cell).stream().filter(e -> !knownSafe.contains(e))
                    .collect(Collectors.toSet());
            double probability = 1.0 / (double) unsureNeighbours.size();
            for (var unsureCell : unsureNeighbours) {
                probabilities[unsureCell.getRow()][unsureCell.getColumn()] += probability;
            }
        }
        for (var cell : knownMines) {
            probabilities[cell.getRow()][cell.getColumn()] = 1.0;
        }
    }

    private Position getNextMove() {
        if (board.isGameEnded())
            return null;
        var move = getSafeMove();
        if (move != null) {
            Logger.getGlobal().info("Making safe move");
            return move;
        } else {
            Logger.getGlobal().info("Making random move");
            return getRandomMove();
        }
    }

    private Position getSafeMove() {
        if (uncovered.equals(knownSafe))
            return null;
        for (var move : knownSafe) {
            if (!uncovered.contains(move))
                return move;
        }
        return null;
    }

    private Position getRandomMove() {
        var unknownCells = allCells.stream().filter(e -> !knownMines.contains(e) && !uncovered.contains(e))
                .collect(Collectors.toSet());
        assert (unknownCells.size() > 0);
        var minProbability = Double.MAX_VALUE;
        for (var cell : unknownCells) {
            if (probabilities[cell.getRow()][cell.getColumn()] < minProbability)
                minProbability = probabilities[cell.getRow()][cell.getColumn()];
        }
        assert (minProbability < Double.MAX_VALUE);
        var minCells = new ArrayList<Position>();
        for (var cell : unknownCells) {
            if (probabilities[cell.getRow()][cell.getColumn()] == minProbability)
                minCells.add(cell);
        }
        assert (minCells.size() > 0);
        var rand = generator.nextInt(minCells.size());
        return minCells.get(rand);
    }
}
