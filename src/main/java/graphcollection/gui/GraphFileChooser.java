/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Рома
 */
public class GraphFileChooser {
    private final JFileChooser chooser = new JFileChooser();

    public GraphFileChooser() {
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileNameExtensionFilter("Txt files", "txt"));
        chooser.setAcceptAllFileFilterUsed(false);
    }

    private void setTextFilter() {
        chooser.setFileFilter(new FileNameExtensionFilter("Txt files", "txt"));
    }

    private void setImageFilter() {
        chooser.setFileFilter(new FileNameExtensionFilter("PNG files", "png"));
    }

    private File getFile(Component parent) {
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = new File(chooser.getSelectedFile().getPath());
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new InternalError(e);
            }
            return file;
        } else {
            return null;
        }
    }

    public File openFile(Component parent) {
        setTextFilter();
        return (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION ?
                chooser.getSelectedFile() : null);
    }

    public File saveFile(Component parent) {
        setTextFilter();
        return getFile(parent);
    }

    public File saveImageFile(Component parent) {
        setImageFilter();
        return getFile(parent);
    }

}
