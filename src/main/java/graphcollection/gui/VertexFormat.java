/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Рома
 */
public class VertexFormat {
    public static final String format = "[a-zA-Zа-яА-Я0-9]{1,10}";

    public static final String getRegEx() {
        return "^" + format + "$";
    }

    public static boolean isFormat(String v) {
        Pattern p = Pattern.compile(getRegEx());
        Matcher m = p.matcher(v);
        return m.matches();
    }
}
