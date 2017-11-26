package org.elias.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * Shows GUI dialogs
 */
@Component
public class DialogManager {

    @Autowired
    private JFrame mainFrame;

    /**
     * Shows error dialog. Users must click OK before continuing
     *
     * @param message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
    }


}
