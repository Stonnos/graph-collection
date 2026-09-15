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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Рома
 */
public class ReferenceBase extends JFrame {

    private JTextArea textInfo;
    private final Component aboutProgrammComponent;

    /**
     * Конструктор
     *
     * @param component
     */
    public ReferenceBase(Component component) {
        this.setResizable(false);
        this.setTitle("О программе");
        this.setLayout(new GridBagLayout());
        aboutProgrammComponent = component;
        aboutProgrammComponent.setEnabled(false);
        //-------------------------------------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                aboutProgrammComponent.setEnabled(true);
            }
        });
        //-------------------------------------
        textInfo = new JTextArea(25, 65);
        textInfo.setWrapStyleWord(true);
        textInfo.setLineWrap(true);
        textInfo.setEditable(false);
        JScrollPane scrollPanel = new JScrollPane(textInfo);
        //----------------------------------------
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        //-----------------------------------
        okButton.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent evt) {
                                           aboutProgrammComponent.setEnabled(true);
                                           dispose();
                                       }
                                   }
        );
        //-----------------------------------
        menuPanel.add(okButton);
        //----------------------------------------
        add(scrollPanel);
        add(menuPanel);
        //-----------------------------------------------------------------
        add(scrollPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 15, 2, 15), 0, 0));
        add(menuPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 15, 5, 15), 0, 0));
        //-----------------------------------------------------------------
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Метод для чтения информации из файла с именем name
     *
     * @param name
     */
    public final void readInfoFromFile(String name) {
        String fileName = System.getProperty("user.dir")
                + "/Descriptions/" + name;
        try (FileInputStream in = new FileInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, "Cp1251"))) {
            String line;
            textInfo.setText("");
            while ((line = reader.readLine()) != null) {
                textInfo.append(line + System.getProperty("line.separator"));
            }
            textInfo.setCaretPosition(0);
        } catch (IOException e) {
            textInfo.setText(e.toString());
        }
    }
}

