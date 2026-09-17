package graphcollection.gui;

import javax.swing.*;
import java.awt.*;

public class SimpleResultsPanel extends AlgorithmsResultsPanel {
    private static final Dimension PREFERRED_SIZE = new Dimension(400, 200);
    private final String result;

    public SimpleResultsPanel(Component component, String result) {
        super(component, "Результаты алгоритма");
        this.result = result;
        this.setPreferredSize(PREFERRED_SIZE);
    }

    @Override
    protected String getSelectedText() {
        return result;
    }

    @Override
    protected Component createResults() {
        var textArea = new JTextArea(15, 30);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setText(result);
        textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        textArea.setCaretPosition(0);
        return new JScrollPane(textArea);
    }
}
