package graphcollection.gui.popup;

import graphcollection.gui.util.ButtonUtils;
import graphcollection.gui.util.PanelBorderUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Application popups service.
 */
@Slf4j
public class PopupService {

    private static final int MAX_POPUPS_AT_TIME = 3;

    private static final long POPUP_VISIBILITY_TIME_MILLIS = 4000L;

    private static final int POPUP_MARGIN_RIGNT = 225;
    private static final int POPUP_MARGIN_TOP = 80;
    private static final int POPUP_MARGIN_BOTTOM = 45;
    private static final Dimension PANEL_DIMENSION = new Dimension(225, 70);

    private final PopupFactory popupFactory = PopupFactory.getSharedInstance();

    private final ConcurrentLinkedDeque<PopupDescriptor> popups = new ConcurrentLinkedDeque<>();

    /**
     * Popup descriptor.
     */
    @Data
    @AllArgsConstructor
    public static class PopupDescriptor {

        /**
         * Popup component
         */
        private Popup popup;
        /**
         * Popup x value
         */
        private int x;
        /**
         * Popup y value
         */
        private int y;
    }

    /**
     * Shows popup with specified message.
     *
     * @param infoMessage - info message
     * @param component   - parent component
     */
    public void showErrorPopup(String infoMessage, Component component) {
        createAndAddPopupDescriptor(infoMessage, component).ifPresent(popupDescriptor -> new Thread(() -> {
            popupDescriptor.getPopup().show();
            try {
                Thread.sleep(POPUP_VISIBILITY_TIME_MILLIS);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
                Thread.currentThread().interrupt();
            } finally {
                popupDescriptor.getPopup().hide();
                popups.remove(popupDescriptor);
            }
        }).start());
    }

    private synchronized Optional<PopupDescriptor> createAndAddPopupDescriptor(String infoMessage,
                                                                               Component component) {
        if (popups.size() == MAX_POPUPS_AT_TIME) {
            return Optional.empty();
        }
        PopupDescriptor popupDescriptor = createInfoMessagePopup(infoMessage, component);
        popups.addLast(popupDescriptor);
        return Optional.of(popupDescriptor);
    }

    private int calculatePopupY(Component component) {
        if (popups.isEmpty()) {
            return component.getY() + POPUP_MARGIN_TOP;
        } else {
            PopupDescriptor last = popups.getLast();
            return last.getY() + 2 * POPUP_MARGIN_BOTTOM;
        }
    }

    private PopupDescriptor createInfoMessagePopup(String message, Component component) {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setPreferredSize(PANEL_DIMENSION);
        infoPanel.setMaximumSize(PANEL_DIMENSION);
        infoPanel.setMinimumSize(PANEL_DIMENSION);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(PanelBorderUtils.createEtchedBorder());
        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.RED);
        JButton closeButton = ButtonUtils.createCloseButton();
        infoPanel.add(messageLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5), 0, 0));
        infoPanel.add(closeButton, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 0, 4, 0), 0, 0));
        int x = component.getX() + POPUP_MARGIN_RIGNT;
        int y = calculatePopupY(component);
        Popup popup = popupFactory.getPopup(component, infoPanel, x, y);
        PopupDescriptor popupDescriptor = new PopupDescriptor(popup, x, y);
        closeButton.addActionListener(evt -> {
            popup.hide();
            popups.remove(popupDescriptor);
        });
        return popupDescriptor;
    }
}
