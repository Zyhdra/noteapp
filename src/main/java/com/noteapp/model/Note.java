package com.noteapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a text note with content, file path, and modification tracking.
 */
public class Note implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Required attributes (at least 2)
    private String content;
    private String filePath;
    private LocalDateTime lastModified;
    private boolean isModified;
    
    /**
     * Creates a new empty note.
     */
    public Note() {
        this.content = "";
        this.filePath = null;
        this.lastModified = LocalDateTime.now();
        this.isModified = false;
    }
    
    /**
     * Creates a note with content.
     */
    public Note(String content) {
        this.content = content != null ? content : "";
        this.filePath = null;
        this.lastModified = LocalDateTime.now();
        this.isModified = false;
    }
    
    // Getters
    public String getContent() {
        return content;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    public boolean isModified() {
        return isModified;
    }
    
    public String getFileName() {
        if (filePath == null) {
            return "Untitled";
        }
        return filePath.substring(filePath.lastIndexOf('\\') + 1);
    }
    
    // Setters
    public void setContent(String content) {
        this.content = content != null ? content : "";
        this.lastModified = LocalDateTime.now();
        this.isModified = true;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public void setModified(boolean modified) {
        this.isModified = modified;
    }
    
    public void markAsSaved() {
        this.isModified = false;
        this.lastModified = LocalDateTime.now();
    }
}
