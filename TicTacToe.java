import java.util.Scanner;

public class TicTacToe {
    private static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {
        int mode = 0;

        // Read which mode to play with
        do {
            System.out.println("Enter 1 for 2 players, 2 for playing against the CPU, or 0 to quit.");
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
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        } while (true);

        input.close();
    }
}