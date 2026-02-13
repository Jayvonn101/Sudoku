package org.sudoku;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Sudoku sudoku;
    private long elapsedTime;
    private LocalDateTime saveTime;
    private String difficulty;
    
    public GameState(Sudoku sudoku, long elapsedTime, String difficulty) {
        this.sudoku = sudoku;
        this.elapsedTime = elapsedTime;
        this.difficulty = difficulty;
        this.saveTime = LocalDateTime.now();
    }
    
    public Sudoku getSudoku() { return sudoku; }
    public long getElapsedTime() { return elapsedTime; }
    public String getDifficulty() { return difficulty; }
    public LocalDateTime getSaveTime() { return saveTime; }
    
    public String getFormattedSaveTime() {
        return saveTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getFormattedElapsedTime() {
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        long seconds = elapsedTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    public void save(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    
    public static GameState load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameState) ois.readObject();
        }
    }
}
