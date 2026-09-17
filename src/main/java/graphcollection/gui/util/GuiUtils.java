package graphcollection.gui.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Optional;

@UtilityClass
public class GuiUtils {

    public static void showToolTipProgrammatically(JComponent component) {
        showToolTipProgrammatically(component, component.getWidth() / 2, component.getHeight());
    }

    public static void showToolTipProgrammatically(JComponent component, int x, int y ) {
        if (component.getToolTipText() == null) {
            return;
        }
        MouseEvent event = new MouseEvent(
                component,
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false
        );
        // 4. Force ToolTipManager to read the event immediately
        ToolTipManager.sharedInstance().mouseMoved(event);
    }

    public static boolean isEmpty(JTextField jTextField) {
        return !Optional.ofNullable(jTextField).map(JTextField::getText).isPresent()
                || StringUtils.isEmpty(jTextField.getText().trim());
    }
}
