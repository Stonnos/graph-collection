/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Рома
 */
public class GraphsTable extends JDialog {
    private JTable table;
    private boolean dialogResult;
    
    public GraphsTable(JTable table, JFrame component) {
        super(component,"Таблица доступных графов",true);
       // this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.table = table;
        //-------------------------------------
        //-------------------------------------
        JScrollPane scrollPanel = new JScrollPane(table);
        //----------------------------------------
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        //-----------------------------------
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (table.getSelectedRow() != -1) {
                    dialogResult = true;
                    setVisible(false);
                }
                else {
                    JOptionPane.showMessageDialog(GraphsTable.this,
                            "Граф не выбран!",
                           null, JOptionPane.WARNING_MESSAGE);
                }
            }
         }
        );
        //-----------------------------------
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dialogResult = false;
                setVisible(false);
            }
         }
        );
        //-----------------------------------
        menuPanel.add(okButton);
        menuPanel.add(cancelButton);
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
        this.setLocationRelativeTo(component);
    }
    
    public boolean dialog() {
        return dialogResult;
    }
    
    public String getGraphName() {
        return (String)table.getValueAt(table.getSelectedRow(), 0);
    }

}
