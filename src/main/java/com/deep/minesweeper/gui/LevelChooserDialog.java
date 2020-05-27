package com.deep.minesweeper.gui;

import com.deep.minesweeper.data.GameLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

public class LevelChooserDialog extends javax.swing.JDialog {

    private JLabel columnLabel;
    private JSpinner columnSpinner;
    private JSpinner mineSpinner;
    private JLabel minesLabel;
    private JLabel rowLabel;
    private JSpinner rowSpinner;
    private SelectorState selectorState;
    private GameLevel level;

    public GameLevel getLevel() {
        return level;
    }

    public LevelChooserDialog(Frame parent, boolean modal) {
        super(parent, modal);
        this.selectorState = SelectorState.BEGINNER;
        this.level = GameLevel.BEGINNER;

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getGlobal().severe(ex.toString());
        }

        initComponents();

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {

        ButtonGroup choiceButtonGroup = new ButtonGroup();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        JLabel instructionLabel = new JLabel();
        JRadioButton beginnerRadioButton = new JRadioButton();
        JRadioButton intermediateRadioButton = new JRadioButton();
        JRadioButton expertRadioButton = new JRadioButton();
        JRadioButton customRadioButton = new JRadioButton();
        rowLabel = new JLabel();
        columnSpinner = new JSpinner();
        columnLabel = new JLabel();
        rowSpinner = new JSpinner();
        minesLabel = new JLabel();
        mineSpinner = new JSpinner();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Choose difficulty level");
        setModal(true);
        setResizable(false);

        okButton.setText("Ok");
        okButton.addActionListener(this::okButtonActionPerformed);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelButtonActionPerformed);

        instructionLabel.setText("Choose the difficulty level you want:");

        choiceButtonGroup.add(beginnerRadioButton);
        beginnerRadioButton.setSelected(true);
        beginnerRadioButton.setText("Beginner (10x10, 10 mines)");
        beginnerRadioButton.addActionListener(this::beginnerRadioButtonActionPerformed);

        choiceButtonGroup.add(intermediateRadioButton);
        intermediateRadioButton.setText("Intermediate (16x16, 40 mines)");
        intermediateRadioButton.addActionListener(this::intermediateRadioButtonActionPerformed);

        choiceButtonGroup.add(expertRadioButton);
        expertRadioButton.setText("Expert (16x30, 99 mines)");
        expertRadioButton.addActionListener(this::expertRadioButtonActionPerformed);

        choiceButtonGroup.add(customRadioButton);
        customRadioButton.setText("Custom");
        customRadioButton.addActionListener(this::customRadioButtonActionPerformed);

        rowLabel.setText("Rows");
        rowLabel.setEnabled(false);

        columnSpinner.setModel(new SpinnerNumberModel(10, 0, null, 1));
        columnSpinner.setEnabled(false);

        columnLabel.setText("Columns");
        columnLabel.setEnabled(false);

        rowSpinner.setModel(new SpinnerNumberModel(10, 0, null, 1));
        rowSpinner.setEnabled(false);

        minesLabel.setText("Mines");
        minesLabel.setEnabled(false);

        mineSpinner.setModel(new SpinnerNumberModel(10, 0, null, 1));
        mineSpinner.setEnabled(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(okButton)
                                        .addGap(4, 4, 4)
                                        .addComponent(cancelButton))
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(instructionLabel))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(43, 43, 43)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(expertRadioButton)
                                                                .addComponent(intermediateRadioButton)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                        .addComponent(customRadioButton)
                                                                                        .addGap(26, 26, 26))
                                                                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                                .addComponent(columnLabel)
                                                                                                .addComponent(rowLabel)
                                                                                                .addComponent(minesLabel))
                                                                                        .addGap(18, 18, 18)))
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addComponent(rowSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(mineSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(columnSpinner, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(43, 43, 43)
                                                        .addComponent(beginnerRadioButton)))
                                        .addGap(0, 80, Short.MAX_VALUE)))
                        .addContainerGap())
        );

        layout.linkSize(SwingConstants.HORIZONTAL, cancelButton, okButton);

        layout.linkSize(SwingConstants.HORIZONTAL, columnSpinner, mineSpinner, rowSpinner);

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(instructionLabel)
                        .addGap(18, 18, 18)
                        .addComponent(beginnerRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(intermediateRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expertRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(columnLabel)
                                                .addComponent(columnSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(okButton)
                                                .addComponent(cancelButton))
                                        .addContainerGap())
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 41, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(rowSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(rowLabel))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(mineSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(minesLabel))
                                        .addGap(78, 78, 78))))
        );

        beginnerRadioButton.getAccessibleContext().setAccessibleDescription("");

        pack();
    }

    private void okButtonActionPerformed(ActionEvent evt) {
        switch (selectorState) {
            case BEGINNER -> level = GameLevel.BEGINNER;
            case INTERMEDIATE -> level = GameLevel.INTERMEDIATE;
            case EXPERT -> level = GameLevel.EXPERT;
            case CUSTOM -> level = new GameLevel((Integer) rowSpinner.getValue(),
                    (Integer) columnSpinner.getValue(),
                    (Integer) mineSpinner.getValue(),
                    "Custom");
        }
        setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    private void beginnerRadioButtonActionPerformed(ActionEvent evt) {
        setCustomSettingsState(false);
        selectorState = SelectorState.BEGINNER;
    }

    private void intermediateRadioButtonActionPerformed(ActionEvent evt) {
        setCustomSettingsState(false);
        selectorState = SelectorState.INTERMEDIATE;
    }

    private void expertRadioButtonActionPerformed(ActionEvent evt) {
        setCustomSettingsState(false);
        selectorState = SelectorState.EXPERT;
    }

    private void customRadioButtonActionPerformed(ActionEvent evt) {
        setCustomSettingsState(true);
        selectorState = SelectorState.CUSTOM;
    }

    private void setCustomSettingsState(boolean state) {
        rowLabel.setEnabled(state);
        columnLabel.setEnabled(state);
        minesLabel.setEnabled(state);
        rowSpinner.setEnabled(state);
        columnSpinner.setEnabled(state);
        mineSpinner.setEnabled(state);
    }

    private enum SelectorState {
        BEGINNER, INTERMEDIATE, EXPERT, CUSTOM
    }

}

