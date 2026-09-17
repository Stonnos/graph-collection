package graphcollection.gui.algorithms;

import graphcollection.gui.AlgorithmsResultsPanel;

import javax.swing.*;
import java.awt.*;

public class DfsResultsPanel extends AlgorithmsResultsPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(450, 300);
    private final String paths;
    private final String discovery;
    private final String finishing;

    private JTabbedPane tabbedPane;

    public DfsResultsPanel(Component component,
                           String paths,
                           String discovery,
                           String finishing) {
        super(component, "Обход в глубину (DFS)");
        this.paths = paths;
        this.discovery = discovery;
        this.finishing = finishing;
        this.setPreferredSize(PREFERRED_SIZE);
    }

    @Override
    protected String getSelectedText() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
        return textArea.getText();
    }

    @Override
    protected Component createResults() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(tabbedPane.getFont().deriveFont(Font.BOLD));
        var pathsScrollPanel = createTextScrollPane(paths);
        var discoveryScrollPanel = createTextScrollPane(discovery);
        var finishingScrollPanel = createTextScrollPane(finishing);
        tabbedPane.add("Структура леса", pathsScrollPanel);
        tabbedPane.add("Метки обнаружения", discoveryScrollPanel);
        tabbedPane.add("Метки завершения", finishingScrollPanel);
        return tabbedPane;
    }

    private JScrollPane createTextScrollPane(String text) {
        var textArea = new JTextArea(15, 35);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setText(text);
        textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        textArea.setCaretPosition(0);
        return new JScrollPane(textArea);
    }
}
