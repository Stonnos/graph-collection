package graphcollection.gui.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.regex.Pattern;

public class VertexNameDocument extends LengthDocument {

    private static final String VERTEX_NAME_FORMAT = "^[a-zA-Zа-яА-Я0-9]{1,10}$";

    public VertexNameDocument(int length) {
        super(length);
    }

    @Override
    public boolean format(String str) {
        return Pattern.compile(VERTEX_NAME_FORMAT).matcher(str).matches();
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (format(str)) {
            super.insertString(offs, str, a);
        }
    }

}
