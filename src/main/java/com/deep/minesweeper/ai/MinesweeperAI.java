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
    private final Random generator;

    public MinesweeperAI(MinesweeperBoardData board) {
        this.board = board;
        this.sentences = new ArrayList<>();
        this.knownMines = new HashSet<>();
        this.knownSafe = new HashSet<>();
        this.generator = new Random();
        this.uncovered = new HashSet<>();
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
        }
        Logger.getGlobal().info(String.format("Uncovered size: %d Mine count: %d Safe count: %d Sentences size: %d",
                uncovered.size(),
                knownMines.size(),
                knownSafe.size(),
                sentences.size()
        ));
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

        var uncoveredThisMove = modifiedUncovered.stream()
                .filter(c -> !uncovered.contains(c))
                .collect(Collectors.toSet());

        for (var move : uncoveredThisMove) {
            var neighbours = board.getNeighbours(move);
            var safeNeighbours = neighbours.stream()
                    .filter(knownSafe::contains).collect(Collectors.toSet());
            var mineNeighbours = neighbours.stream()
                    .filter(knownMines::contains).collect(Collectors.toSet());

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

    private Position getNextMove() {
        if (board.isGameEnded()) return null;
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
        if (uncovered.equals(knownSafe)) return null;
        for (var move : knownSafe) {
            if (!uncovered.contains(move)) return move;
        }
        return null;
    }

    private Position getRandomMove() {
        var bound = board.getRows() * board.getColumns();
        var rand = generator.nextInt(bound);
        var position = new Position(rand / board.getColumns(), rand % board.getColumns());
        while (uncovered.contains(position) || knownMines.contains(position)) {
            rand = generator.nextInt(bound);
            position = new Position(rand / board.getColumns(), rand % board.getColumns());
        }
        return position;
    }
}
