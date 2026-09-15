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
public class SptResultsFrame extends AlgorithmsResultsFrame {
      
      private final String dist, paths;
      
      public SptResultsFrame(Component component, String title, String d, String p) {
          super(component,d);
          this.dist = d;
          this.paths = p;
          this.setTitle(title);
          JMenuBar menu = new JMenuBar();
          this.setJMenuBar(menu);
          JMenu sptMenu = new JMenu("Кратчайшие пути");
          menu.add(sptMenu);
          JMenuItem distMenu = new JMenuItem("Расстояния");
          JMenuItem pathsMenu = new JMenuItem("Структуры путей");
          sptMenu.add(distMenu);
          sptMenu.add(pathsMenu);
          distMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setResults(dist);
                //--------------------------------------------------------
            }
          });
          pathsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setResults(paths);
                //--------------------------------------------------------
            }
          });
      }
}
