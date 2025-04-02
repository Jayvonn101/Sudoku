package Game_Generator;
import java.util.Random;


public class RandomSpot extends RandomNum {

    public RandomSpot(int row, int col) {
            super(row, col);
        }
        private int row;
    private int col;
    private int [][] grid;

    public void numbers (int row, int col){
        this.row = row ;
        this.col = col;
        grid = new int[row][col];

    }
    public void fillNums(){
        Random rand = new Random();
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                grid[i][j] = rand.nextInt(row + 1);
            }
        }
    }
    public void printNums(){
        for (int[] gridRow : grid){
            for (int num : gridRow){
                System.out.print(num + " ");
            }
        }
    }
}
