package graphcollection.gui;

import com.formdev.flatlaf.FlatLightLaf;
import graphcollection.gui.config.ConfigurationService;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.util.Locale;

/**
 *
 * @author Рома
 */

public class GraphCollection {

    private static final ConfigurationService CONFIG_SERVICE = ConfigurationService.getApplicationConfigService();
    private static final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        Locale.setDefault(RUSSIAN_LOCALE);
        // Sets Look and feel UI theme
        UIManager.setLookAndFeel(new FlatLightLaf());
        CONFIG_SERVICE.loadUiTextProperties();
        // Register FontAwesome icon fonts
        IconFontSwing.register(FontAwesome.getIconFont());
        JGraphFrame graph = new JGraphFrame();
        graph.setVisible(true);
    }

}
