/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * 
 * @author Рома
 */
public class AlgorithmsResultsFrame extends JFrame {
   
    private JTextArea textInfo;
    private Component component;
    
    /**
     * 
     * @param component
     * @param result 
     */
    public AlgorithmsResultsFrame(Component component, String result) {
        this.setResizable(false);
        //this.setLocationRelativeTo(null);
        this.setTitle("Результаты алгоритма");
        this.setLayout(new GridBagLayout());
        this.component = component;
        component.setEnabled(false);
        //-------------------------------------
        addWindowListener(new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent evt) {
                 component.setEnabled(true);
             }
        });
        //-------------------------------------
        textInfo = new JTextArea(15,30);
        textInfo.setWrapStyleWord(true);
        textInfo.setLineWrap(true);
        textInfo.setEditable(false);
        setResults(result);
        JScrollPane scrollPanel = new JScrollPane(textInfo);
        //----------------------------------------
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveFileButton = new JButton("Сохранить");
        JButton okButton = new JButton("OK");
        //-----------------------------------
        saveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               try {
                  GraphFileChooser fileChooser = new GraphFileChooser();
                  File file = fileChooser.saveFile(AlgorithmsResultsFrame.this);
                  if (file != null) {
                      saveToFile(file.getPath());
                  }
                }
                catch(InternalError | Exception e) {
                    JOptionPane.showMessageDialog(AlgorithmsResultsFrame.this,e,
                            null, JOptionPane.ERROR_MESSAGE);
                }                
            }
         }
        );
        //-----------------------------------
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                component.setEnabled(true);
                dispose();
            }
         }
        );
        //-----------------------------------
        menuPanel.add(saveFileButton);
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
    
    protected final void setResults(String text) {
        textInfo.setText(text);
        textInfo.setCaretPosition(0);
    }
    /**
     * Метод для чтения информации из файла с именем name
     * @param name 
     */
    public final void saveToFile(String name) {
        try (FileOutputStream out = new FileOutputStream(name);
              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"Cp1251")) )
        {
            writer.write(textInfo.getText());
        }
        catch (IOException e) {
           JOptionPane.showMessageDialog(AlgorithmsResultsFrame.this,e,
                            "", JOptionPane.ERROR_MESSAGE);
        }
    }
}
