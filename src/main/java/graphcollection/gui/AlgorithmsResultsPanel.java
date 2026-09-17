package graphcollection.gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static graphcollection.gui.ButtonUtils.createButton;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class AlgorithmsResultsPanel extends JPanel {

    private static final PopupFactory POPUP_FACTORY = new PopupFactory();
    private static final int POPUP_MARGIN_LEFT = 25;
    private static final int POPUP_MARGIN_TOP = 100;

    private final Component component;

    private Popup popup;

    public AlgorithmsResultsPanel(Component component, String title) {
        this.component = component;
        this.setLayout(new GridBagLayout());
        this.setBorder(PanelBorderUtils.createTitledBorder(title));
    }

    public void initialize() {
        initUiComponents();
    }

    public void showPanel() {
        int x = component.getX() + component.getWidth() - (int) this.getPreferredSize().getWidth() - POPUP_MARGIN_LEFT;
        int y = component.getY() + POPUP_MARGIN_TOP;
        popup = POPUP_FACTORY.getPopup(component, this, x, y);
        popup.show();
    }

    public void hidePanel() {
        Optional.ofNullable(popup).ifPresent(Popup::hide);
    }

    private void initUiComponents() {
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveFileButton = createButton("Сохранить");
        JButton okButton = createButton("Закрыть");
        saveFileButton.addActionListener(evt -> {
            try {
                GraphFileChooser fileChooser = new GraphFileChooser();
                fileChooser.setSelectedFile(new File("results.txt"));
                File file = fileChooser.saveFile(component);
                if (file != null) {
                    saveToFile(file.getPath());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(component, ex,
                        null, JOptionPane.ERROR_MESSAGE);
            }
        }
        );
        okButton.addActionListener(evt -> Optional.ofNullable(popup).ifPresent(Popup::hide));
        menuPanel.add(saveFileButton);
        menuPanel.add(okButton);
        add(createResults(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 15, 2, 15), 0, 0));
        add(menuPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 15, 3, 15), 0, 0));
    }

    private void saveToFile(String name) {
        try (FileOutputStream out = new FileOutputStream(name);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.write(getSelectedText());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(component, ex, EMPTY, JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract String getSelectedText();

    protected abstract Component createResults();
}
