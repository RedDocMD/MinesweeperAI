package com.deep.minesweeper;

import com.deep.minesweeper.data.MinesweeperBoardData;
import com.deep.minesweeper.gui.LevelChooserDialog;
import com.deep.minesweeper.gui.MinesweeperFrame;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var levelChooser = new LevelChooserDialog(null, true);
            levelChooser.setVisible(true);
            var boardData = new MinesweeperBoardData(levelChooser.getLevel());
            Logger.getGlobal().info("\n" + boardData.toString());
            var frame = new MinesweeperFrame(boardData);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
