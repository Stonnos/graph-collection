/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Рома
 */
public class DFSResultsFrame extends AlgorithmsResultsFrame {
    
    private final String paths, discovery, finishing;
    
    public DFSResultsFrame(Component component, 
             String p, String d, String f) {
          super(component,p);
          this.setTitle("Обход в глубину (DFS)");
          this.paths = p;
          this.discovery = d;
          this.finishing = f;
          JMenuBar menu = new JMenuBar();
          this.setJMenuBar(menu);
          JMenu dfsMenu = new JMenu("Результаты DFS");
          menu.add(dfsMenu);
          JMenuItem pathsMenu = new JMenuItem("Структура леса");
          JMenuItem discoveryMenu = new JMenuItem("Метки обнаружения");
          JMenuItem finishingMenu = new JMenuItem("Метки завершения");
          dfsMenu.add(pathsMenu);
          dfsMenu.add(discoveryMenu);
          dfsMenu.add(finishingMenu);
          pathsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setResults(paths);
                //--------------------------------------------------------
            }
          });
          discoveryMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setResults(discovery);
                //--------------------------------------------------------
            }
          });
          finishingMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setResults(finishing);
                //--------------------------------------------------------
            }
          });
      }
    
}
