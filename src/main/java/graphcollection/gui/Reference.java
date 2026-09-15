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

/**
 *
 * @author Рома
 */
public class Reference extends ReferenceBase {

    public Reference(Component component) {
        super(component);
        this.setTitle("Справка");
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JMenu userMenu = new JMenu("Меню");
        menu.add(userMenu);
        JMenuItem userRef = new JMenuItem("Руководство пользователя");
        JMenuItem literature = new JMenuItem("Используемые источники");
        userMenu.add(userRef);
        userMenu.add(literature);
        userMenu.addSeparator();
        JMenu algorithms = new JMenu("Алгоритмы");
        userMenu.add(algorithms);
        JMenu directedGraphAlgoritms = new
                JMenu("Алгоритмы для орграфов");
        JMenu undirectedGraphAlgoritms = new
                JMenu("Алгоритмы для неорграфов");
        algorithms.add(directedGraphAlgoritms);
        algorithms.add(undirectedGraphAlgoritms);
        //------------------------------------------------------
        userRef.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent evt) {
                                          readInfoFromFile("Руководство пользователя.txt");
                                      }
                                  }
        );
        literature.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent evt) {
                                             readInfoFromFile("Используемые источники.txt");
                                         }
                                     }
        );
        //-------------------------------------------------------
        JMenuItem euler = new JMenuItem("Поиск Эйлерова цикла");
        JMenuItem gamilton = new JMenuItem("Поиск Гамильтонова цикла");
        JMenuItem mst = new JMenuItem("Алгоритм минимального покрывающего дерева");
        JMenuItem undirectedOther = new JMenuItem("Другие алгоритмы");
        undirectedGraphAlgoritms.add(euler);
        undirectedGraphAlgoritms.add(gamilton);
        undirectedGraphAlgoritms.add(mst);
        undirectedGraphAlgoritms.add(undirectedOther);
        //--------------------------------------------------------
        euler.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        readInfoFromFile("Поиск Эйлерова цикла.txt");
                                    }
                                }
        );
        gamilton.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent evt) {
                                           readInfoFromFile("Поиск Гамильтонова цикла.txt");
                                       }
                                   }
        );
        mst.addActionListener(new ActionListener() {
                                  @Override
                                  public void actionPerformed(ActionEvent evt) {
                                      readInfoFromFile("Алгоритм минимального покрывающего дерева.txt");
                                  }
                              }
        );
        undirectedOther.addActionListener(new ActionListener() {
                                              @Override
                                              public void actionPerformed(ActionEvent evt) {
                                                  readInfoFromFile("Дополнительные алгоритмы для неорграфов.txt");
                                              }
                                          }
        );
        //------------------------------------------------------------------
        JMenuItem topoSort = new JMenuItem("Топологическая сортировка");
        JMenuItem spt = new JMenuItem("Кратчайшие пути из одной вершины");
        JMenuItem allSpt = new JMenuItem("Кратчайшие пути между всеми парами вершин");
        JMenuItem directedOther = new JMenuItem("Другие алгоритмы");
        directedGraphAlgoritms.add(topoSort);
        directedGraphAlgoritms.add(spt);
        directedGraphAlgoritms.add(allSpt);
        directedGraphAlgoritms.add(directedOther);
        //--------------------------------------------------------
        topoSort.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent evt) {
                                           readInfoFromFile("Топологическая сортировка.txt");
                                       }
                                   }
        );
        spt.addActionListener(new ActionListener() {
                                  @Override
                                  public void actionPerformed(ActionEvent evt) {
                                      readInfoFromFile("Кратчайшие пути из одной вершины.txt");
                                  }
                              }
        );
        allSpt.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent evt) {
                                         readInfoFromFile("Кратчайшие пути между всеми парами вершин.txt");
                                     }
                                 }
        );
        directedOther.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent evt) {
                                                readInfoFromFile("Дополнительные алгоритмы для орграфов.txt");
                                            }
                                        }
        );
    }

} //End of class Reference
