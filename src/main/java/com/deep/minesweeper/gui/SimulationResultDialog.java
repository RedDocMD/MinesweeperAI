package com.deep.minesweeper.gui;

import com.deep.minesweeper.data.SimulationResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

public class SimulationResultDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 711704662492653660L;
    private JLabel roundInfo;
    private JLabel timeInfo;
    private JLabel totalInfo;
    private JLabel wonInfo;

    public SimulationResultDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            Logger.getGlobal().severe(ex.toString());
        }
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        JPanel contentPanel = new JPanel();
        JLabel totalLabel = new JLabel();
        JLabel wonLabel = new JLabel();
        JLabel roundLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        totalInfo = new JLabel();
        wonInfo = new JLabel();
        roundInfo = new JLabel();
        timeInfo = new JLabel();
        JButton closeButton = new JButton();

        setTitle("Simulation Results");

        contentPanel.setLayout(new GridBagLayout());

        totalLabel.setText("Total games:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new Insets(15, 10, 5, 0);
        contentPanel.add(totalLabel, gridBagConstraints);

        wonLabel.setText("Games won:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new Insets(5, 10, 5, 0);
        contentPanel.add(wonLabel, gridBagConstraints);

        roundLabel.setText("Average rounds:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new Insets(5, 10, 5, 0);
        contentPanel.add(roundLabel, gridBagConstraints);

        timeLabel.setText("Average time:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new Insets(5, 10, 15, 0);
        contentPanel.add(timeLabel, gridBagConstraints);

        totalInfo.setText("games");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(15, 11, 5, 10);
        contentPanel.add(totalInfo, gridBagConstraints);

        wonInfo.setText("games");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(5, 11, 5, 10);
        contentPanel.add(wonInfo, gridBagConstraints);

        roundInfo.setText("rounds");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(5, 12, 5, 10);
        contentPanel.add(roundInfo, gridBagConstraints);

        timeInfo.setText("ms");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(5, 13, 15, 10);
        contentPanel.add(timeInfo, gridBagConstraints);

        closeButton.setText("Close");
        closeButton.addActionListener(this::closeButtonActionPerformed);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(contentPanel, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE).addComponent(closeButton)))
                                .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup().addContainerGap()
                .addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton).addContainerGap()));

        pack();
    }

    private void closeButtonActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    public void showResult(SimulationResult result) {
        totalInfo.setText(result.getGamesPlayed() + " games");
        wonInfo.setText(result.getGamesWon() + " games");
        roundInfo.setText(result.getAverageRoundCount() + " rounds");
        timeInfo.setText(result.getAverageTimeInMs() + " ms");
        setVisible(true);
    }
}
