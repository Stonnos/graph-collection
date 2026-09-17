package graphcollection.gui;

import javax.swing.*;
import java.awt.*;

public class Reference extends BaseReference {

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
        userRef.addActionListener(evt -> readInfoFromFile("Руководство пользователя.html")
        );
        literature.addActionListener(evt -> readInfoFromFile("Используемые источники.html")
        );
        JMenuItem euler = new JMenuItem("Поиск Эйлерова цикла");
        JMenuItem gamilton = new JMenuItem("Поиск Гамильтонова цикла");
        JMenuItem mst = new JMenuItem("Алгоритм минимального покрывающего дерева");
        JMenuItem undirectedOther = new JMenuItem("Другие алгоритмы");
        undirectedGraphAlgoritms.add(euler);
        undirectedGraphAlgoritms.add(gamilton);
        undirectedGraphAlgoritms.add(mst);
        undirectedGraphAlgoritms.add(undirectedOther);
        euler.addActionListener(evt -> readInfoFromFile("Поиск Эйлерова цикла.html")
        );
        gamilton.addActionListener(evt -> readInfoFromFile("Поиск Гамильтонова цикла.html")
        );
        mst.addActionListener(evt -> readInfoFromFile("Алгоритм минимального покрывающего дерева.html")
        );
        undirectedOther.addActionListener(evt -> readInfoFromFile("Дополнительные алгоритмы для неорграфов.html")
        );
        JMenuItem topoSort = new JMenuItem("Топологическая сортировка");
        JMenuItem spt = new JMenuItem("Кратчайшие пути из одной вершины");
        JMenuItem allSpt = new JMenuItem("Кратчайшие пути между всеми парами вершин");
        JMenuItem directedOther = new JMenuItem("Другие алгоритмы");
        directedGraphAlgoritms.add(topoSort);
        directedGraphAlgoritms.add(spt);
        directedGraphAlgoritms.add(allSpt);
        directedGraphAlgoritms.add(directedOther);
        topoSort.addActionListener(evt -> readInfoFromFile("Топологическая сортировка.html")
        );
        spt.addActionListener(evt -> readInfoFromFile("Кратчайшие пути из одной вершины.html")
        );
        allSpt.addActionListener(evt -> readInfoFromFile("Кратчайшие пути между всеми парами вершин.html")
        );
        directedOther.addActionListener(evt -> readInfoFromFile("Дополнительные алгоритмы для орграфов.html")
        );
    }

}
