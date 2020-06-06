package com.deep.minesweeper.gui;

import com.deep.minesweeper.data.MinesweeperBoardData;
import com.deep.minesweeper.data.Position;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Cell extends JPanel {

    private static final long serialVersionUID = -2016279651477232739L;
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 30;
    private static final Border COVERED_BORDER = BorderFactory.createRaisedBevelBorder();
    private static final Border UNCOVERED_BORDER = BorderFactory.createLoweredBevelBorder();
    private static final Color[] LABEL_COLORS = {
            Color.BLACK,  // 0
            Color.BLUE,   // 1
            Color.GREEN.darker(),  // 2
            Color.RED,    // 3
            Color.CYAN,   // 4
            Color.ORANGE, // 5
            Color.YELLOW, // 6
            Color.GRAY,   // 7
            Color.MAGENTA // 8
    };
    private static final String MINE_IMAGE_PATH = "/images/mine.png";
    private static final String FLAG_IMAGE_PATH = "/images/flag.png";
    private static final String WRONG_FLAG_IMAGE_PATH = "/images/wrong_flag.png";

    private final Image mineImage = new ImageIcon(Cell.class.getResource(MINE_IMAGE_PATH)).getImage();
    private final Image flagImage = new ImageIcon(Cell.class.getResource(FLAG_IMAGE_PATH)).getImage();
    private final Image wrongFlagImage = new ImageIcon(Cell.class.getResource(WRONG_FLAG_IMAGE_PATH)).getImage();

    private final int row;
    private final int column;
    private final MinesweeperBoardData boardData;
    private final JLabel label;
    private boolean drawMine;
    private boolean drawFlag;
    private boolean drawWrongFlag;

    public Cell(int row, int column, MinesweeperBoardData boardData) {
        this.row = row;
        this.column = column;
        this.boardData = boardData;
        this.label = new JLabel("", JLabel.CENTER);
        this.drawMine = false;
        this.drawFlag = false;
        this.drawWrongFlag = false;
        initComponent();
    }

    private void initComponent() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBorder(COVERED_BORDER);
        add(label, BorderLayout.NORTH);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position getPosition() {
        return new Position(row, column);
    }

    public void computeCellState() {
        var state = boardData.getMineState(row, column);
        if (state == MinesweeperBoardData.Element.COVERED_EMPTY || state == MinesweeperBoardData.Element.COVERED_MINE) {
            setBorder(COVERED_BORDER);
            label.setText("");
            drawMine = false;
            drawFlag = false;
            drawWrongFlag = false;
        } else {
            setBorder(UNCOVERED_BORDER);
            if (state == MinesweeperBoardData.Element.UNCOVERED_EMPTY) {
                drawMine = false;
                drawFlag = false;
                var value = boardData.getMineCount(row, column);
                if (value != 0) {
                    label.setText(value + "");
                    label.setForeground(LABEL_COLORS[value]);
                } else {
                    label.setText("");
                }
            } else if (state == MinesweeperBoardData.Element.FLAGGED) {
                drawFlag = true;
                drawMine = false;
                drawWrongFlag = false;
            } else if (state == MinesweeperBoardData.Element.UNCOVERED_MINE) {
                drawMine = true;
                drawFlag = false;
                drawWrongFlag  = false;
            } else if (state == MinesweeperBoardData.Element.WRONGLY_FLAGGED) {
                drawWrongFlag = true;
                drawMine = false;
                drawFlag = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (drawMine)
            g.drawImage(mineImage, 5, 0, null);
        if (drawFlag) {
            g.drawImage(flagImage, 0, 2, null);
        }
        if (drawWrongFlag) {
            g.drawImage(wrongFlagImage, 0, 2, null);
        }
    }
}
