package org.sudoku;

import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Sudoku game;
    private static long startTime;
    private static String difficulty;
    private static final String SAVE_DIR = "saves/";
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("     Welcome to Sudoku!");
        System.out.println("=================================\n");
        
        new File(SAVE_DIR).mkdirs();
        
        boolean running = true;
        while (running) {
            showMainMenu();
            String choice = getInput("Enter choice: ");
            
            switch (choice) {
                case "1":
                    newGame();
                    break;
                case "2":
                    loadGame();
                    break;
                case "3":
                    showInstructions();
                    break;
                case "4":
                    System.out.println("\nThanks for playing! Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. New Game");
        System.out.println("2. Load Game");
        System.out.println("3. Instructions");
        System.out.println("4. Exit");
        System.out.println();
    }
    
    private static void showInstructions() {
        System.out.println("\n--- How to Play ---");
        System.out.println("Fill the 9x9 grid so that each row, column, and 3x3 box");
        System.out.println("contains all digits from 1 to 9.");
        System.out.println("\nCommands:");
        System.out.println("  p - Place a number (e.g., 'p 3 5 7' places 7 at row 3, col 5)");
        System.out.println("  r - Remove a number (e.g., 'r 3 5' removes number at row 3, col 5)");
        System.out.println("  h - Get a hint");
        System.out.println("  s - Solve the puzzle automatically");
        System.out.println("  save - Save current game");
        System.out.println("  quit - Quit to main menu");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private static void newGame() {
        System.out.println("\n--- Select Difficulty ---");
        System.out.println("1. Easy (30 empty cells)");
        System.out.println("2. Medium (45 empty cells)");
        System.out.println("3. Hard (55 empty cells)");
        System.out.println("4. Custom");
        
        String choice = getInput("Enter choice: ");
        int emptyCells;
        
        switch (choice) {
            case "1":
                emptyCells = 30;
                difficulty = "Easy";
                break;
            case "2":
                emptyCells = 45;
                difficulty = "Medium";
                break;
            case "3":
                emptyCells = 55;
                difficulty = "Hard";
                break;
            case "4":
                emptyCells = getIntInput("Enter number of empty cells (1-80): ", 1, 80);
                difficulty = "Custom";
                break;
            default:
                System.out.println("Invalid choice. Starting Easy game.");
                emptyCells = 30;
                difficulty = "Easy";
        }
        
        System.out.println("\nGenerating puzzle...");
        game = new Sudoku(9, 9);
        game.fillNums();
        game.placeEmptyCells(emptyCells);
        startTime = System.currentTimeMillis();
        
        System.out.println("\nDifficulty: " + difficulty);
        playGame();
    }
    
    private static void loadGame() {
        File saveDir = new File(SAVE_DIR);
        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".sav"));
        
        if (saveFiles == null || saveFiles.length == 0) {
            System.out.println("\nNo saved games found.");
            return;
        }
        
        System.out.println("\n--- Saved Games ---");
        for (int i = 0; i < saveFiles.length; i++) {
            System.out.println((i + 1) + ". " + saveFiles[i].getName());
        }
        System.out.println((saveFiles.length + 1) + ". Back");
        
        int choice = getIntInput("Select save file: ", 1, saveFiles.length + 1);
        if (choice == saveFiles.length + 1) return;
        
        try {
            GameState state = GameState.load(saveFiles[choice - 1].getPath());
            game = state.getSudoku();
            startTime = System.currentTimeMillis() - (state.getElapsedTime() * 1000);
            difficulty = state.getDifficulty();
            System.out.println("\nGame loaded successfully!");
            System.out.println("Difficulty: " + difficulty);
            System.out.println("Saved on: " + state.getFormattedSaveTime());
            playGame();
        } catch (Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }
    
    private static void playGame() {
        boolean playing = true;
        
        while (playing && !game.isSolved()) {
            game.printGrid();
            System.out.println("Time: " + getElapsedTime());
            System.out.println("Commands: p=place, r=remove, h=hint, s=solve, save=save, quit=quit");
            
            String input = getInput("\nEnter command: ").trim().toLowerCase();
            String[] parts = input.split("\\s+");
            
            switch (parts[0]) {
                case "p":
                    if (parts.length == 4) {
                        handlePlace(parts);
                    } else {
                        System.out.println("Usage: p <row> <col> <number>");
                    }
                    break;
                case "r":
                    if (parts.length == 3) {
                        handleRemove(parts);
                    } else {
                        System.out.println("Usage: r <row> <col>");
                    }
                    break;
                case "h":
                    handleHint();
                    break;
                case "s":
                    handleSolve();
                    break;
                case "save":
                    handleSave();
                    break;
                case "quit":
                case "q":
                    playing = false;
                    break;
                default:
                    System.out.println("Unknown command. Try: p, r, h, s, save, quit");
            }
        }
        
        if (game.isSolved()) {
            game.printGrid();
            System.out.println("\n" + "=".repeat(40));
            System.out.println("    Congratulations! Puzzle solved!");
            System.out.println("    Time: " + getElapsedTime());
            System.out.println("    Difficulty: " + difficulty);
            System.out.println("=".repeat(40) + "\n");
        }
    }
    
    private static void handlePlace(String[] parts) {
        try {
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;
            int num = Integer.parseInt(parts[3]);
            game.placeNum(row, col, num);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers. Please use integers.");
        }
    }
    
    private static void handleRemove(String[] parts) {
        try {
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;
            game.removeNum(row, col);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers. Please use integers.");
        }
    }
    
    private static void handleHint() {
        Sudoku.Hint hint = game.getHint();
        if (hint != null) {
            System.out.println("\nHint: Try placing " + hint.value + " at row " + (hint.row + 1) + ", column " + (hint.col + 1));
        } else {
            System.out.println("No hints available - puzzle is complete!");
        }
    }
    
    private static void handleSolve() {
        String confirm = getInput("Are you sure you want to auto-solve? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("Solving...");
            game.solve();
        }
    }
    
    private static void handleSave() {
        String filename = getInput("Enter save name: ");
        if (!filename.endsWith(".sav")) {
            filename += ".sav";
        }
        
        try {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            GameState state = new GameState(game, elapsed, difficulty);
            state.save(SAVE_DIR + filename);
            System.out.println("Game saved successfully as: " + filename);
        } catch (Exception e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }
    
    private static String getElapsedTime() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long hours = elapsed / 3600;
        long minutes = (elapsed % 3600) / 60;
        long seconds = elapsed % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
