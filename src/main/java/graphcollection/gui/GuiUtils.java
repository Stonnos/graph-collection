package graphcollection.gui;

import lombok.experimental.UtilityClass;

import javax.swing.*;
import java.awt.event.MouseEvent;

@UtilityClass
public class GuiUtils {

    public static void showToolTipProgrammatically(JComponent component) {
        if (component.getToolTipText() == null) {
            return;
        }
        int x = component.getWidth() / 2;
        int y = component.getHeight() / 2;
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
}
