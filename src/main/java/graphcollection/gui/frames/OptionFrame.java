package graphcollection.gui.frames;


import javax.swing.*;
import java.awt.*;

import static graphcollection.gui.util.ButtonUtils.createButton;

public class OptionFrame extends JDialog {

    private final JSlider slider;
    private boolean dialogResult = false;
    private static final int DELIMITER = 1000;

    public boolean dialogResult() {
        return dialogResult;
    }

    public int getAnimationSpeed() {
        return slider.getValue() * DELIMITER;
    }

    public OptionFrame(JFrame parent, int animationSpeed) {
        super(parent, "Настройки", true);
        this.setResizable(false);
        this.setLocation(350, 200);
        this.setLayout(new GridBagLayout());
        this.add(new JLabel("Задержка:"), new GridBagConstraints(0, 0, 3, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(10, 5, 10, 5), 0, 0));

        slider = new JSlider();
        slider.setValue(animationSpeed / DELIMITER);

        slider.setMaximum(10);
        slider.setMinimum(1);
        slider.setToolTipText(animationSpeed + " ms.");
        this.add(slider, new GridBagConstraints(0, 1, 3, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        JButton okButton = createButton("OK");
        JButton cancelButton = createButton("Отмена");

        slider.addChangeListener(evt -> slider.setToolTipText(getAnimationSpeed() + " ms."));

        okButton.addActionListener(evt -> {
            dialogResult = true;
            setVisible(false);
        });

        cancelButton.addActionListener(evt -> {
            dialogResult = false;
            setVisible(false);
        });

        this.add(okButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 15, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 15, 5), 0, 0));
        this.pack();
        this.setLocationRelativeTo(parent);
    }
}
