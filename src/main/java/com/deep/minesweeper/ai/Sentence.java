package com.deep.minesweeper.ai;

import com.deep.minesweeper.data.Position;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Sentence {
    private final Set<Position> cells;
    private final int mineCount;
    private final Set<Position> knownSafe;
    private final Set<Position> knownMine;

    public Sentence(Set<Position> cells, int mineCount, Set<Position> knownSafe, Set<Position> knownMine) {
        this.cells = cells;
        this.mineCount = mineCount;
        this.knownSafe = knownSafe;
        this.knownMine = knownMine;
    }

    public Set<Position> getKnownSafe() {
        return knownSafe;
    }

    public Set<Position> getKnownMine() {
        return knownMine;
    }

    public void doSelfInference() {
        if (mineCount == cells.size() - knownSafe.size() && mineCount != knownMine.size()) {
            knownMine.addAll(cells.stream()
                    .filter(c -> !knownSafe.contains(c))
                    .collect(Collectors.toSet()));
        }
        if (mineCount == 0) {
            knownSafe.addAll(cells);
        }
        if (mineCount == knownMine.size()) {
            knownSafe.addAll(cells);
            knownSafe.removeAll(knownMine);
        }
    }

    public Sentence doMutualInference(Sentence other) {
        if (cells.containsAll(other.cells)) {
            var newCells = cells.stream()
                    .filter(c -> !other.cells.contains(c))
                    .collect(Collectors.toSet());
            var newMineCount = mineCount - other.mineCount;
            var newKnownSafe = knownSafe.stream()
                    .filter(newCells::contains)
                    .collect(Collectors.toSet());
            var newKnownMine = knownMine.stream()
                    .filter(newCells::contains)
                    .collect(Collectors.toSet());

            var newSentence = new Sentence(newCells, newMineCount, newKnownSafe, newKnownMine);
            newSentence.doSelfInference();

            return newSentence;
        } else {
            return null;
        }
    }

    public void markMines(Set<Position> mines) {
        var newCells = mines.stream()
                .filter(cells::contains)
                .collect(Collectors.toSet());
        knownMine.addAll(newCells);
        knownSafe.removeAll(newCells);
    }

    public void markSafes(Set<Position> safes) {
        var newCells = safes.stream()
                .filter(cells::contains)
                .collect(Collectors.toSet());
        knownSafe.addAll(newCells);
        knownMine.removeAll(newCells);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return cells.equals(sentence.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells);
    }
}
