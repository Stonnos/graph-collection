package graphcollection.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.regex.Pattern;

public class IntegerDocument extends LengthDocument {

    public static final String INT_FORMAT = "^[0-9]*$";

    public IntegerDocument(int length) {
        super(length);
    }

    @Override
    public boolean format(String str) {
        return Pattern.compile(INT_FORMAT).matcher(str).matches();
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (format(str)) {
            super.insertString(offs, str, a);
        }
    }

}
