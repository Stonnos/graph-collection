/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Рома
 */
public class VertexInputDialog extends JDialog {
    
     private JTextField source;
     private JTextField target;
     private boolean dialogResult = false;
     
     public VertexInputDialog(JFrame parent) {
        super(parent,"Задание вершин",true);
        this.setResizable(false);
        this.setLocation(350,200);
        this.setLayout(new GridBagLayout());
        //-----------------------------------------
        this.add(new JLabel("Вершина U:"),new GridBagConstraints(0, 0, 1, 1, 0, 0, 
            GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 10, 5), 0, 0));
        source = new JTextField(8);
        target = new JTextField(8);
        this.add(source,new GridBagConstraints(1, 0, 1, 1, 0, 0, 
            GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        this.add(new JLabel("Вершина V:"),new GridBagConstraints(0, 1, 1, 1, 0, 0, 
            GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 10, 5), 0, 0));
        this.add(target,new GridBagConstraints(1, 1, 1, 1, 0, 0, 
            GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        //----------------------------------------------------------------------------
        JButton okButton = new JButton("  OK  ");
        JButton cancelButton = new JButton("Cancel");
        //--------------------------------------------
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {               
                dialogResult = true;
                setVisible(false);                
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
        this.add(okButton, new GridBagConstraints(0, 2, 1, 1, 1, 1, 
          GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 15, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 2, 1, 1, 1, 1, 
          GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 15, 5), 0, 0));
        this.pack();
        this.setLocationRelativeTo(parent);
        source.requestFocusInWindow();
     }
     
     public String source() {
         return source.getText();
     }
     
     public String target() {
         return target.getText();
     }
     
     public boolean dialogResult() {
         return dialogResult;
     }
}
