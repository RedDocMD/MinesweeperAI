package com.deep.minesweeper.gui;

import com.deep.minesweeper.data.MinesweeperBoardData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class MinesweeperPanel extends JPanel {
    private final MinesweeperBoardData boardData;
    private final MinesweeperFrame parent;
    private final Cell[][] cells;
    private final JPanel backPanel;
    private final int rows;
    private final int columns;

    public MinesweeperPanel(MinesweeperBoardData boardData, MinesweeperFrame parent) {
        super();
        this.boardData = boardData;
        this.parent = parent;
        this.rows = boardData.getRows();
        this.columns = boardData.getColumns();
        this.cells = new Cell[rows][columns];
        this.backPanel = new JPanel();
        initComponent();
    }

    private void initComponent() {
        // TODO: Initialize layout and listeners
        backPanel.setLayout(new GridLayout(rows, columns));
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                cells[i][j] = new Cell(i, j, boardData);
                cells[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 1) {
                            if (!boardData.isGameEnded() && parent.isHumanPlaying()) {
                                var cell = (Cell) e.getComponent();
                                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0) {
                                    // Click with no Ctrl
                                    boardData.uncoverCell(cell.getRow(), cell.getColumn());
                                } else if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                                    // Click with Ctrl
                                    boardData.flagCell(cell.getRow(), cell.getColumn());
                                    parent.updateFlagged();
                                }
                                recomputeCellsState();
                                parent.announceGameEnd();
                            } else if (boardData.isGameEnded()) {
                                parent.announceResetGame();
                            }
                        } else if (e.getClickCount() >= 2) {
                            if (!boardData.isGameEnded() && parent.isHumanPlaying()) {
                                var cell = (Cell) e.getComponent();
                                Logger.getGlobal().info("Double clicked: " + cell.getPosition());
                                boardData.uncoverNeighbours(cell.getPosition());
                                recomputeCellsState();
                                parent.announceGameEnd();
                            } else if (boardData.isGameEnded()) {
                                parent.announceResetGame();
                            }
                        }
                        repaint();
                    }
                });
                backPanel.add(cells[i][j]);
            }
        }
        add(backPanel);
    }

    public void recomputeCellsState() {
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                cells[i][j].computeCellState();
            }
        }
    }
}
