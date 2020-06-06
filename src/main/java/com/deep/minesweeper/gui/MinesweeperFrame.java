package com.deep.minesweeper.gui;

import com.deep.minesweeper.ai.MinesweeperAI;
import com.deep.minesweeper.data.MinesweeperBoardData;
import com.deep.minesweeper.data.SimulationResult;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinesweeperFrame extends JFrame {

    private static final long serialVersionUID = -5399265131477804304L;
    private static final String ICON_PATH = "/images/minesweeper.png";
    private final MinesweeperPanel board;
    private final MinesweeperBoardData data;
    private final MinesweeperAI ai;
    private final JPanel humanPanel;
    private final JPanel aiPanel;
    private final JPanel buttonPanel;
    private final JButton aiButton;
    private final JButton playButton;
    private final JButton resetButton;
    private final JButton simulateButton;
    private final JPanel infoPanel;
    private final JLabel rowsLabel;
    private final JLabel columnsLabel;
    private final JLabel minesLabel;
    private final JLabel flaggedLabel;
    private boolean aiPlaying;
    private boolean humanPlaying;

    public MinesweeperFrame(MinesweeperBoardData data) {
        super();
        this.board = new MinesweeperPanel(data, this);
        this.data = data;
        this.ai = new MinesweeperAI(data);
        this.humanPanel = new JPanel();
        this.aiPanel = new JPanel();
        this.buttonPanel = new JPanel();
        this.aiButton = new JButton("AI make move");
        this.resetButton = new JButton("Reset Board");
        this.playButton = new JButton("Play game");
        this.simulateButton = new JButton("Simulate game");
        this.infoPanel = new JPanel();
        this.rowsLabel = new JLabel("Rows: " + data.getRows());
        this.columnsLabel = new JLabel("Columns: " + data.getColumns());
        this.minesLabel = new JLabel("Mines: " + data.getTotalMines());
        this.flaggedLabel = new JLabel("Flagged: " + data.getFlaggedCount());
        this.humanPlaying = false;
        this.aiPlaying = false;
        initComponents();
    }

    public boolean isHumanPlaying() {
        return humanPlaying;
    }

    public void updateFlagged() {
        flaggedLabel.setText("Flagged: " + data.getFlaggedCount());
    }

    private void initComponents() {
        resetButton.addActionListener(e -> {
            resetGame();
            Logger.getGlobal().info("\n" + data.toString());
        });

        playButton.addActionListener(e -> {
            humanPlaying = true;
            aiPlaying = false;
            aiButton.setEnabled(false);
            playButton.setEnabled(false);
            simulateButton.setEnabled(false);
        });

        aiButton.addActionListener(e -> {
            aiPlaying = true;
            humanPlaying = false;
            playButton.setEnabled(false);
            simulateButton.setEnabled(false);
            aiMakeMove();
        });

        simulateButton.addActionListener(e -> {
            var level = Logger.getGlobal().getLevel();
            Logger.getGlobal().setLevel(Level.OFF);
            final int DEFAULT_GAMES = 10;
            var ans = JOptionPane.showInputDialog(this, "Enter the number of games to simulate:", "Simulate",
                    JOptionPane.INFORMATION_MESSAGE);
            var games = DEFAULT_GAMES;
            try {
                games = Integer.parseInt(ans);
            } catch (NumberFormatException ignored) {
            }
            var result = runSimulation(games);
            showResult(result);
            Logger.getGlobal().setLevel(level);
        });

        aiPanel.setLayout(new FlowLayout());
        aiPanel.add(aiButton);
        aiPanel.add(simulateButton);

        humanPanel.setLayout(new FlowLayout());
        humanPanel.add(playButton);
        humanPanel.add(resetButton);

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(humanPanel, BorderLayout.NORTH);
        buttonPanel.add(aiPanel, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.SOUTH);

        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 4));
        infoPanel.add(rowsLabel);
        infoPanel.add(columnsLabel);
        infoPanel.add(minesLabel);
        infoPanel.add(flaggedLabel);

        add(infoPanel, BorderLayout.NORTH);

        add(board, BorderLayout.CENTER);
        setResizable(false);
        setTitle("Minesweeper AI");
        setIconImage(new ImageIcon(MinesweeperFrame.class.getResource(ICON_PATH)).getImage());
        pack();
    }

    private void resetGame() {
        data.resetBoard();
        board.recomputeCellsState();
        updateFlagged();
        playButton.setEnabled(true);
        aiButton.setEnabled(true);
        simulateButton.setEnabled(true);
        aiPlaying = false;
        humanPlaying = false;
        ai.reset();
    }

    private SimulationResult runSimulation(int games) {
        final int ACCEPTABLE_ROUND_COUNT = 4;
        int roundCount = 0;
        int winCount = 0;
        int points = 0;
        long time = 0;

        for (var i = 0; i < games;) {
            int rounds = 0;
            var startTime = System.nanoTime();
            while (!data.isGameEnded()) {
                ++rounds;
                ai.makeMove();
            }
            var endTime = System.nanoTime();
            var currPoints = data.getPoint();
            if (rounds <= ACCEPTABLE_ROUND_COUNT && currPoints <= 2500) {
                resetGame();
                continue;
            }
            if (data.getGameState() == MinesweeperBoardData.GameState.WON)
                ++winCount;
            roundCount += rounds;
            points += currPoints;
            time += endTime - startTime;
            resetGame();
            ++i;
        }
        return new SimulationResult(winCount, games, (double) roundCount / (double) games, time, points / games);
    }

    private void showResult(SimulationResult result) {
        System.out.println(result.toString());
    }

    private void aiMakeMove() {
        ai.makeMove();
        board.recomputeCellsState();
        updateFlagged();
        repaint();
        if (data.isGameEnded()) {
            aiButton.setEnabled(false);
            announceGameEnd();
        }
    }

    public void announceGameEnd() {
        var state = data.getGameState();
        var user = aiPlaying ? "AI has " : "You have ";
        if (state == MinesweeperBoardData.GameState.WON)
            JOptionPane.showMessageDialog(this, user + "won!", "Game over", JOptionPane.INFORMATION_MESSAGE);
        else if (state == MinesweeperBoardData.GameState.LOST)
            JOptionPane.showMessageDialog(this, user + "lost!", "Game over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void announceResetGame() {
        JOptionPane.showMessageDialog(this, "The game is over!\nPress the Reset button to start again", "Game over",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
