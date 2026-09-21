package graphcollection.gui.frames;

import graphcollection.gui.util.ResourceUtils;

import javax.swing.*;
import java.awt.*;

import static graphcollection.gui.util.ButtonUtils.createButton;

public class BaseReference extends JFrame {

    private static final String CONTENT_TYPE = "text/html";
    private static final int PREFERRED_WIDTH = 750;
    private static final int PREFERRED_HEIGHT = 500;

    private final JTextPane textInfo;

    public BaseReference() {
        this.setResizable(false);
        this.setTitle("О программе");
        this.setLayout(new GridBagLayout());


        textInfo = new JTextPane();
        textInfo.setContentType(CONTENT_TYPE);
        textInfo.setEditable(false);
        textInfo.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        JScrollPane scrollPanel = new JScrollPane(textInfo);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = createButton("OK");
        okButton.addActionListener(evt -> dispose());

        menuPanel.add(okButton);
        add(scrollPanel);
        add(menuPanel);
        add(scrollPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 15, 2, 15), 0, 0));
        add(menuPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 15, 5, 15), 0, 0));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void readInfoFromFile(String fileName) {
        textInfo.setText(ResourceUtils.load(fileName));
        textInfo.setCaretPosition(0);
    }
}

