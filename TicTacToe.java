import java.util.*;

public class TicTacToe {
    // a 3x3 board
    // 1 -> occupied by player1
    // 2 -> occupied by player2
    // 0 -> unoccupied
    private static int[][] board = new int[3][3];
    private static int consecutiveError = 0;                // number of consecutive invalid inputs
    private static int[] numError = {0, 0};                 // number of invalid inputs
    private static Scanner input = new Scanner(System.in);  

    // cpu will not play selected positions (start from 1~9)
    private static ArrayList<Integer> openPos = new ArrayList<>();

    public static void main(String[] args) {
        int mode = 0;
        boolean exitGame = false;

        // Read which mode to play with
        do {
            System.out.println("Enter 1 for 2-player mode / Enter 2 for CPU mode / Enter 0 to quit");
            mode = input.nextInt();

            switch (mode) {
                case 1:
                    twoPlayers();
                    break;
                case 2:
                    againstCPU();
                    break;
                case 0:
                    System.out.println("Thanks for playing!");
                    exitGame = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        } while (!exitGame);

        input.close();
    }

    public static void twoPlayers() {
        initBoard();
        int currentPlayer = 1;
        boolean error = false;

        while (true) {
            if (!error) {
                displayBoard();
            }

            System.out.print("Player " + currentPlayer + "'s turn: ");
            int position = input.nextInt();

            if (position == 0) {
                System.out.println("Player " + currentPlayer + " forfeits the game.");
                int winner = (currentPlayer == 1 ? 2 : 1);
                System.out.println("Game Over! Player " + winner + " wins!");
                return;
            }

            if (isValid(position)) {
                error = false;
                consecutiveError = 0;
                changeBoard(currentPlayer, position);

                // Check if the game ends
                if (checkWin(currentPlayer)) {
                    displayBoard();
                    System.out.println("Game Over! Player " + currentPlayer + " wins!");
                    return;
                } else if (checkTie()) {
                    displayBoard();
                    System.out.println("Game Over! It's a draw!");
                    return;
                } else {
                    // switch player
                    currentPlayer = (currentPlayer == 1 ? 2 : 1);
                }
            } else {
                System.out.println("Incorrect entry, please try again.");
                error = true;
                consecutiveError++;
                numError[currentPlayer-1]++;

                // 'and' OR 'or'???
                if (consecutiveError >= 3 || numError[currentPlayer-1] >= 5) {
                    System.out.println("Player " + currentPlayer + " forfeit the game due to reaching maximum incorrect entries.");
                    int winner = (currentPlayer == 1 ? 2 : 1);
                    System.out.println("Game Over! Player " + winner + " wins!");
                    return;
                }
            }
        }
    }

    public static void againstCPU(){

        //player 1 is you, player 2 is cpu
        int currentPlayer = 1;

        //fill with 1~9
        openPos.clear();
        for (int i = 1; i <= 9; i ++){
            openPos.add(i);
        }

        initBoard();
        boolean error = false;
        int position = 0;

        while (true) {

            //display the board if theres no error
            if (!error) {
                
                //display board once
                if (currentPlayer == 1){
                    displayBoard();
                }
            }

            //your turn
            if (currentPlayer == 1){
                System.out.print("Player 1's turn: ");
                position = input.nextInt();
            }

            //cpu turn
            else {
                
                boolean clearWin = findClearWin();
                
                //MAKE SURE IT DOESNT RANDOMIZE WHEN THERES A CLEAR WIN
                if (clearWin){
                    int numberWin = selectClearWin(2);
                    int numberDef = selectClearWin(1);

                    //if clear win, return clearWinPos
                    if (numberWin != 0){
                        position = numberWin;
                    }

                    //else if clear defense, return clearWinPos
                    else if (numberDef != 0){
                        position = numberDef;
                    }
                }

                else {
                    position = selectRandomPos();
                }
            }
            
            //process input
            if (position == 0) {
                System.out.println("Player 1 forfeits the game.");
                System.out.println("Game Over! CPU wins!");
                return;
            }

            if (isValid(position)) {
                error = false;
                consecutiveError = 0;
                changeBoard(currentPlayer, position);

                //this position is selected, remove it from cpu's option
                openPos.remove(Integer.valueOf(position));

                //check for ends
                if (checkWin(currentPlayer)) {
                    displayBoard();
                    
                    if (currentPlayer == 1){
                        System.out.println("Game Over! Player " + currentPlayer + " wins!");
                    }
                    else {
                        System.out.println("Game Over! CPU wins!");
                    }
                    
                    return;
                }
                
                else if (checkTie()) {
                    displayBoard();
                    System.out.println("Game Over! It's a draw!");
                    return;
                }
                
                //if game doesnt end
                else {
                    //switch player
                    currentPlayer = (currentPlayer == 1 ? 2 : 1);
                }
            }
            
            else {
                System.out.println("Incorrect entry, please try again.");
                error = true;

                //ASK ABOUT THIS LATER
                consecutiveError++;
                numError[currentPlayer-1]++;

                // 'and' OR 'or'???
                if (consecutiveError >= 3 || numError[currentPlayer-1] >= 5) {
                    System.out.println("Player " + currentPlayer + " forfeit the game due to reaching maximum incorrect entries.");

                    //this will always display "CPU wins" but its here
                    //just in case the CPU bugs and chooses invalid positions
                    if (currentPlayer == 1){
                        //dumb error handling
                        System.out.println("Game Over! Player " + currentPlayer + " wins!");
                    }
                    else {
                        System.out.println("Game Over! CPU wins!");
                    }

                    return;
                }
            }
        }


    }
    
    public static boolean isValid(int position) {
        if (position < 1 || position > 9) {
            return false;
        }

        int row = (position - 1) / 3;
        int col = (position - 1) % 3;

        if (board[row][col] == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkWin(int player) {
        // row
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        // col
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        // diagonal
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[2][0] == player && board[1][1] == player && board[0][2] == player) {
            return true;
        }

        return false;
    }

    public static boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //cpu finds a clear win / clear defense
    public static boolean findClearWin(){

        boolean found = false;
        int numberWin = selectClearWin(2);
        int numberDef = selectClearWin(1);

        //if clear win, return clearWinPos
        if (numberWin != 0){
            found = true;
        }

        //else if clear defense, return clearWinPos
        else if (numberDef != 0){
            found = true;
        }

        else {
            found = false;
        }

        return found;
    }

    public static int selectRandomPos(){
        //random number 1~9, +1 to avoid cpu from choosing 0
        Random rand = new Random();
        int randPos = 0;
        boolean valid = false;

        //randomize until cpu chooses valid position
        while (!valid){
            randPos = rand.nextInt(9) + 1;

            //go throught cpu's available moves
            for (int i = 0; i < openPos.size(); i ++){

                //choose this available space and remove it
                if (openPos.get(i) == randPos){
                    openPos.remove(i);
                    valid = true;
                    break;
                }
            }
        }
        return randPos;
    }

    public static int selectClearWin(int forWho){
        
        int clearWinPos = 0;

        //check for row wins
        for (int i = 0; i < 3; i++) {
            if (board[i][1] == forWho && board[i][2] == forWho) { 
                //first column (1, 4, 7)
                if (openPos.contains(Integer.valueOf((3 * i) + 1))){
                    clearWinPos = (3 * i) + 1;
                    break;
                }
            }
            else if (board[i][0] == forWho && board[i][2] == forWho) {
                //second column (2, 5, 8)
                if (openPos.contains(Integer.valueOf((3 * i) + 2))){
                    clearWinPos = (3 * i) + 2;
                    break;
                }
            }
            else if (board[i][0] == forWho && board[i][1] == forWho) {
                //third column (3, 6, 9)
                if (openPos.contains(Integer.valueOf((3 * i) + 3))){
                    clearWinPos = (3 * i) + 3;
                    break;
                }
            }
        }
        
        //check for column wins
        for (int i = 0; i < 3; i++) {
            if (board[1][i] == forWho && board[2][i] == forWho) {
                //first row (1, 2, 3)
                if (openPos.contains(Integer.valueOf(i + 1))){
                    clearWinPos = i + 1;
                    break;
                }
            }

            if (board[0][i] == forWho && board[2][i] == forWho) {
                //second row (4, 5, 6)
                if (openPos.contains(Integer.valueOf(i + 4))){
                    clearWinPos = i + 4;
                    break;
                }
            }

            if (board[0][i] == forWho && board[1][i] == forWho) {
                //third row (7, 8, 9)
                if (openPos.contains(Integer.valueOf(i + 7))){
                    clearWinPos = i + 7;
                    break;
                }
            }
        }

        //find diagonal wins
        for (int i = 0; i < 3; i++) {
            if (board[1][1] == forWho && board[i][2] == forWho) {
                //first row and col (1, 7)
                //if i = 0 pos = 7
                //if i = 1 pos = 4
                //if i = 2 pos = 1
                // 7 - (3 * i)
                if (openPos.contains(Integer.valueOf(7 - (3 * i)))){
                    clearWinPos = 7 - (3 * i);
                    break;
                }
            }
            else if ((board[0][0] == forWho && board[2][2] == forWho) 
            || (board[0][2] == forWho && board[2][0] == forWho)) {
                //second row and col (5)
                if (openPos.contains(Integer.valueOf(5))){
                    clearWinPos = 5;
                    break;
                }
            }
            else if (board[1][1] == forWho && board[i][0] == forWho) {
                //third row and col (3, 9)
                //if i = 0 pos = 9
                //if i = 1 pos = 6
                //if i = 2 pos = 3
                // 9 - (3 * i)
                if (openPos.contains(Integer.valueOf(9 - (3 * i)))){
                    clearWinPos = 9 - (3 * i);
                    break;
                }
            }
        }

        return clearWinPos;
    }

    public static void initBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        consecutiveError = 0;
        numError[0] = 0;
        numError[1] = 0;
    }

    public static void displayBoard() {
        int block;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                block = board[i][j];
                switch (block) {
                    // unoccupied
                    case 0:
                        System.out.print(" " + (3 * i + j + 1) + " ");
                        break;
                    // player1
                    case 1:
                        System.out.print(" x ");
                        break;
                    // player2
                    case 2:
                        System.out.print(" O ");
                        break;
                    default:
                        break;
                }

                if (j == 2) {
                    System.out.println();
                    if (i != 2) {
                        System.out.println("---+---+---");
                    }
                } else {
                    System.out.print("|");
                }
            }
        }
        System.out.println();
    }

    public static void changeBoard(int player, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        board[row][col] = player;
    }
}