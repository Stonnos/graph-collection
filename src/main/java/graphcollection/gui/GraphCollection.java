/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import com.formdev.flatlaf.FlatLightLaf;
import graphcollection.graph.*;

import java.util.*;

import graphcollection.algorithms.shortestpaths.*;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;

/**
 *
 * @author Рома
 */

public class GraphCollection {

    private static final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        Locale.setDefault(RUSSIAN_LOCALE);
        // Sets Look and feel UI theme
        UIManager.setLookAndFeel(new FlatLightLaf());
        // Register FontAwesome icon fonts
        IconFontSwing.register(FontAwesome.getIconFont());
        JGraphFrame graph = new JGraphFrame();
        graph.setVisible(true);
    }

}
