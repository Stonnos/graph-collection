package graphcollection.gui.dialog;

import graphcollection.gui.choosers.GraphFileChooser;
import graphcollection.gui.SingletonRegistry;
import graphcollection.gui.text.IntegerDocument;
import graphcollection.gui.text.TextFieldInputVerifier;
import graphcollection.gui.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static graphcollection.gui.util.ButtonUtils.createButton;

public class ClusteringAlgorithmInputDialog extends JDialog {
    private final JTextField numClustersText;
    private final JTextField distancesMatrixText;
    private boolean dialogResult = false;

    public ClusteringAlgorithmInputDialog(JFrame parent) {
        super(parent, "Настройка параметров", true);
        this.setResizable(false);
        this.setLocation(350, 200);
        this.setLayout(new GridBagLayout());

        JLabel numClustersLabel = new JLabel("Число кластеров:");
        numClustersLabel.setFont(numClustersLabel.getFont().deriveFont(Font.BOLD));
        JLabel distancesMatrixLabel = new JLabel("Матрица расстояний:");
        distancesMatrixLabel.setFont(distancesMatrixLabel.getFont().deriveFont(Font.BOLD));
        this.add(numClustersLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 5, 10, 5), 0, 0));
        numClustersText = new JTextField(10);
        numClustersText.setDocument(new IntegerDocument(10));
        numClustersText.setInputVerifier(new TextFieldInputVerifier());
        distancesMatrixText = new JTextField(10);
        distancesMatrixText.setEditable(false);
        distancesMatrixText.setBackground(Color.WHITE);
        this.add(numClustersText, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));
        this.add(distancesMatrixLabel, new GridBagConstraints(0, 1, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(5, 5, 2, 5), 0, 0));
        this.add(distancesMatrixText, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 10, 10, 5), 0, 0));

        JButton loadButton = createButton("Загрузить");

        this.add(loadButton, new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 5, 10, 5), 0, 0));

        JButton okButton = createButton("OK");
        JButton cancelButton = createButton("Отмена");

        okButton.addActionListener(evt -> {
            try {
                if (GuiUtils.isEmpty(numClustersText)) {
                    return;
                }
                if (GuiUtils.isEmpty(distancesMatrixText)) {
                    distancesMatrixText.setToolTipText("Загрузите файл с матрицей расстояний!");
                    GuiUtils.showToolTipProgrammatically(distancesMatrixText, distancesMatrixText.getWidth() / 10,
                            distancesMatrixText.getHeight() / 3);
                    return;
                }
                dialogResult = true;
                setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ClusteringAlgorithmInputDialog.this,
                        "Только целые числа!",
                        "Ошибка ввода!", JOptionPane.WARNING_MESSAGE);
                numClustersText.requestFocusInWindow();
            }
        });

        cancelButton.addActionListener(evt -> {
            dialogResult = false;
            setVisible(false);
        });

        loadButton.addActionListener(evt -> {
            try {
                GraphFileChooser fileChooser = SingletonRegistry.getSingleton(GraphFileChooser.class);
                File file = fileChooser.openFile(ClusteringAlgorithmInputDialog.this);
                if (file != null) {
                    distancesMatrixText.setText(file.getPath());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ClusteringAlgorithmInputDialog.this, ex,
                        null, JOptionPane.ERROR_MESSAGE);
            }
        });

        this.add(okButton, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(20, 5, 10, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(20, 5, 10, 5), 0, 0));
        this.pack();
        this.setLocationRelativeTo(parent);
        numClustersText.requestFocusInWindow();
    }

    public int numClusters() {
        return Integer.parseInt(numClustersText.getText());
    }

    public String matrixFile() {
        return distancesMatrixText.getText();
    }

    public boolean dialogResult() {
        return dialogResult;
    }

}
