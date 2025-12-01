package com.noteapp.service;

import com.noteapp.model.Note;
import java.io.*;

/**
 * Handles file operations for notes (save and load).
 */
public class NoteManager {
    
    /**
     * Saves a note to a text file.
     */
    public void saveNote(Note note, String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        // Ensure .txt extension
        if (!filePath.endsWith(".txt")) {
            filePath += ".txt";
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(note.getContent());
        }
        
        note.setFilePath(filePath);
        note.markAsSaved();
    }
    
    /**
     * Loads a note from a text file.
     */
    public Note loadNote(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        Note note = new Note(content.toString());
        note.setFilePath(filePath);
        note.setModified(false);
        
        return note;
    }
    
    /**
     * Creates a new empty note.
     */
    public Note createNewNote() {
        return new Note();
    }
    
    /**
     * Checks if a file exists.
     */
    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        return new File(filePath).exists();
    }
}
