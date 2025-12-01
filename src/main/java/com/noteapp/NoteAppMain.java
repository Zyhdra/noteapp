package com.noteapp;

import com.noteapp.gui.NoteAppGUI;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Note App application.
 */
public class NoteAppMain {
    
    public static void main(String[] args) {
        // Start the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            NoteAppGUI gui = new NoteAppGUI();
            gui.setVisible(true);
        });
    }
}
