package graphcollection.gui.algorithms;

import javax.swing.*;
import java.awt.*;

public class SptResultsPanel extends AlgorithmsResultsPanel {
    private static final Dimension PREFERRED_SIZE = new Dimension(400, 300);
    private final String dist;
    private final String paths;

    private JTabbedPane tabbedPane;

    public SptResultsPanel(Component component, String title, String dist, String paths) {
        super(component, title);
        this.dist = dist;
        this.paths = paths;
        this.setPreferredSize(PREFERRED_SIZE);
    }

    private JScrollPane createTextScrollPane(String text) {
        var textArea = new JTextArea(15, 30);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setText(text);
        textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        textArea.setCaretPosition(0);
        return new JScrollPane(textArea);
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
        var distScrollPanel = createTextScrollPane(dist);
        var pathsScrollPanel = createTextScrollPane(paths);
        tabbedPane.add("Расстояния", distScrollPanel);
        tabbedPane.add("Структуры путей", pathsScrollPanel);
        return tabbedPane;
    }
}
