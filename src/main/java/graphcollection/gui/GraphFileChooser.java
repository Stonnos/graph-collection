package graphcollection.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GraphFileChooser {
    private final JFileChooser chooser = new JFileChooser();

    public GraphFileChooser() {
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileNameExtensionFilter("Txt files", "txt"));
        chooser.setAcceptAllFileFilterUsed(false);
    }

    private void setTextFilter() {
        chooser.resetChoosableFileFilters();
        chooser.setFileFilter(new FileNameExtensionFilter("Txt files", "txt"));
    }

    private void setImageFilter() {
        chooser.resetChoosableFileFilters();
        chooser.setFileFilter(new FileNameExtensionFilter("PNG files", "png"));
    }

    public void setSelectedFile(File file) {
        chooser.setSelectedFile(file);
    }

    private File getFile(Component parent) {
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            for (FileFilter filter : chooser.getChoosableFileFilters()) {
                if (filter.accept(file)) {
                    return file;
                }
            }
            FileNameExtensionFilter ext = (FileNameExtensionFilter) chooser.getFileFilter();
            file = new File(String.format("%s.%s", file.getAbsolutePath(), ext.getExtensions()[0]));
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
