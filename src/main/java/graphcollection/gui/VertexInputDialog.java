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
import java.util.Optional;

/**
 *
 * @author Рома
 */
public class VertexInputDialog extends JDialog {

    private final JComboBox<String> source;
    private final JComboBox<String> target;
    private boolean dialogResult = false;

    public VertexInputDialog(JFrame parent, String[] vertices) {
        super(parent, "Задание вершин", true);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.add(new JLabel("Вершина U:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        source = new JComboBox<>(vertices);
        target = new JComboBox<>(vertices);
        this.add(source, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        this.add(new JLabel("Вершина V:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        this.add(target, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        JButton okButton = ButtonUtils.createOkButton();
        JButton cancelButton = ButtonUtils.createCancelButton();
        okButton.addActionListener(evt -> {
            dialogResult = true;
            setVisible(false);
        });
        cancelButton.addActionListener(evt -> {
            dialogResult = false;
            setVisible(false);
        });
        this.add(okButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 3), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 5, 5), 0, 0));
        this.getRootPane().setDefaultButton(okButton);
        this.pack();
        this.setLocationRelativeTo(parent);
        source.requestFocusInWindow();
    }

    public String source() {
        return Optional.ofNullable(source.getSelectedItem()).map(String::valueOf).orElse(null);
    }

    public String target() {
        return Optional.ofNullable(target.getSelectedItem()).map(String::valueOf).orElse(null);
    }

    public boolean dialogResult() {
        return dialogResult;
    }
}
