package Game_Generator;
import java.util.Random;
import java.util.Scanner;
// This class is for generating random numbers

public class RandomNum {
    private int row;
    private int col;
    private int[][] grid;

    // Constructor to initialize the grid size
    public RandomNum(int row, int col) {
        this.row = row;
        this.col = col;
        this.grid = new int[row][col];
    }

    // Method to fill the grid with random numbers
    public void fillGrid() {
        Random random = new Random();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = random.nextInt(row + 1); 
            }
        }
    }

    // Method to print the grid
    public void printGrid() {
        for (int[] gridRow : grid) {
            for (int num : gridRow) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of rows: ");
        int rows = scanner.nextInt();
        
        System.out.print("Enter the number of columns: ");
        int cols = scanner.nextInt();
        
        RandomNum randomNum = new RandomNum(rows, cols);
        randomNum.fillGrid();
        randomNum.printGrid();
        
        scanner.close();
    }
}
