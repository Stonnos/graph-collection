package graphcollection.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static graphcollection.gui.ButtonUtils.createButton;

public class BaseReference extends JFrame {

    private static final String CONTENT_TYPE = "text/html";
    private static final int PREFERRED_WIDTH = 750;
    private static final int PREFERRED_HEIGHT = 500;

    private final JTextPane textInfo;
    private final Component aboutProgrammComponent;

    public BaseReference(Component component) {
        this.setResizable(false);
        this.setTitle("О программе");
        this.setLayout(new GridBagLayout());
        aboutProgrammComponent = component;
        aboutProgrammComponent.setEnabled(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                aboutProgrammComponent.setEnabled(true);
            }
        });

        textInfo = new JTextPane();
        textInfo.setContentType(CONTENT_TYPE);
        textInfo.setEditable(false);
        textInfo.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        JScrollPane scrollPanel = new JScrollPane(textInfo);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = createButton("OK");
        okButton.addActionListener(evt -> {
            aboutProgrammComponent.setEnabled(true);
            dispose();
        }
        );

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

