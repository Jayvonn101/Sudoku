package org.sudoku;

public class GameSession {
    private Sudoku sudoku;
    private String difficulty;
    private long startTime;
  
    public GameSession(int emptyCells, String difficulty) {
        this.sudoku = new Sudoku(9, 9);
        this.sudoku.fillNums();
        this.sudoku.placeEmptyCells(emptyCells);
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
    }
    public Sudoku getSudoku() {
        return sudoku;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000; // Return time in seconds
    }
}
