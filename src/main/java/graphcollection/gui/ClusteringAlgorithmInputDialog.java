/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 * @author Рома
 */
public class ClusteringAlgorithmInputDialog extends JDialog {
    private JTextField number;
    private JTextField matrix;
    private boolean dialogResult = false;

    public ClusteringAlgorithmInputDialog(JFrame parent) {
        super(parent, "Настройка параметров", true);
        this.setResizable(false);
        this.setLocation(350, 200);
        this.setLayout(new GridBagLayout());
        //-----------------------------------------
        this.add(new JLabel("Число кластеров:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 10, 5), 0, 0));
        number = new JTextField(10);
        matrix = new JTextField(10);
        matrix.setEditable(false);
        matrix.setBackground(Color.WHITE);
        this.add(number, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        this.add(new JLabel("Матрица расстояний:"), new GridBagConstraints(0, 1, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5), 0, 0));
        this.add(matrix, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        //-----------------------------------------------------------
        JButton loadButton = new JButton("Загрузить");
        //-----------------------------------------------------------
        this.add(loadButton, new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        //----------------------------------------------------------------------------
        JButton okButton = new JButton("  OK  ");
        JButton cancelButton = new JButton("Cancel");
        //--------------------------------------------
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    Integer.parseInt(number.getText());
                    dialogResult = true;
                    setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ClusteringAlgorithmInputDialog.this,
                            "Только целые числа!",
                            "Ошибка ввода!", JOptionPane.WARNING_MESSAGE);
                    number.requestFocusInWindow();
                }
            }
        });
        //-----------------------------------------------
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dialogResult = false;
                setVisible(false);
            }
        });
        //--------------------------------------------
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    GraphFileChooser fileChooser = new GraphFileChooser();
                    File file = fileChooser.openFile(ClusteringAlgorithmInputDialog.this);
                    if (file != null) {
                        matrix.setText(file.getPath());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClusteringAlgorithmInputDialog.this, ex,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //-----------------------------------------------
        this.add(okButton, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 15, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 15, 5), 0, 0));
        this.pack();
        this.setLocationRelativeTo(parent);
        number.requestFocusInWindow();
    }

    public int number() {
        return Integer.parseInt(number.getText());
    }

    public String matrixFile() {
        return matrix.getText();
    }

    public boolean dialogResult() {
        return dialogResult;
    }

}
