package com.noteapp.gui;

import com.noteapp.model.Note;
import com.noteapp.service.NoteManager;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;


 // The Main GUI window for the Note App.
 
public class NoteAppGUI extends JFrame {
    
    private JTextArea textArea;
    private JLabel statusLabel;
    private Note currentNote;
    private NoteManager noteManager;
    
    public NoteAppGUI() {
        this.noteManager = new NoteManager();
        this.currentNote = noteManager.createNewNote();
        initializeUI();
        setupListeners();
    }
    
    private void initializeUI() {
        setTitle("Note App - Untitled");
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        // Add keyboard shortcuts
        newItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Create text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create status bar
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
        
        // Add action listeners
        newItem.addActionListener(e -> newNote());
        openItem.addActionListener(e -> openNote());
        saveItem.addActionListener(e -> saveNote());
        saveAsItem.addActionListener(e -> saveNoteAs());
        exitItem.addActionListener(e -> exitApplication());
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void setupListeners() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { markAsModified(); }
            public void removeUpdate(DocumentEvent e) { markAsModified(); }
            public void changedUpdate(DocumentEvent e) { markAsModified(); }
        });
    }
    
    private void markAsModified() {
        if (!currentNote.isModified()) {
            currentNote.setModified(true);
            updateTitle();
        }
    }
    
    private void newNote() {
        if (!checkSaveBeforeAction()) {
            return;
        }
        
        currentNote = noteManager.createNewNote();
        textArea.setText("");
        updateTitle();
        updateStatus("New note created");
    }
    
    private void openNote() {
        if (!checkSaveBeforeAction()) {
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                currentNote = noteManager.loadNote(file.getAbsolutePath());
                textArea.setText(currentNote.getContent());
                updateTitle();
                updateStatus("Opened: " + file.getName());
            } catch (IOException ex) {
                showError("Error Opening File", "Could not open file: " + ex.getMessage());
            }
        }
    }
    
    private void saveNote() {
        if (currentNote.getFilePath() == null) {
            saveNoteAs();
        } else {
            try {
                currentNote.setContent(textArea.getText());
                noteManager.saveNote(currentNote, currentNote.getFilePath());
                updateTitle();
                updateStatus("Saved");
            } catch (IOException ex) {
                showError("Error Saving File", "Could not save file: " + ex.getMessage());
            }
        }
    }
    
    private void saveNoteAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                currentNote.setContent(textArea.getText());
                noteManager.saveNote(currentNote, file.getAbsolutePath());
                updateTitle();
                updateStatus("Saved as: " + file.getName());
            } catch (IOException ex) {
                showError("Error Saving File", "Could not save file: " + ex.getMessage());
            }
        }
    }
    
    private boolean checkSaveBeforeAction() {
        if (currentNote.isModified()) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save changes?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION
            );
            
            if (result == JOptionPane.YES_OPTION) {
                saveNote();
                return !currentNote.isModified();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
    
    private void exitApplication() {
        if (checkSaveBeforeAction()) {
            System.exit(0);
        }
    }
    
    private void updateTitle() {
        String title = "Note App - " + currentNote.getFileName();
        if (currentNote.isModified()) {
            title += " *";
        }
        setTitle(title);
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
