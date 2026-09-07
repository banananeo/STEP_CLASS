import java.util.Random;
import java.util.Scanner;

public class Problem1 {

    static String[] moves = { "Rock", "Paper", "Scissors" };

    public static void main(String[] args) {
        int N = 5;
        Random random = new Random();
        Scanner sc = new Scanner(System.in);

        int wins = 0, losses = 0, draws = 0;

        String[] roundNumbers = new String[N];
        String[] playerChoices = new String[N];
        String[] computerChoices = new String[N];
        String[] results = new String[N];

        for (int i = 0; i < N; i++) {
            String computerMove = moves[random.nextInt(3)];

            System.out.print("Enter your move (Rock/Paper/Scissors) for round " + (i + 1) + ": ");
            String playerMove = sc.nextLine().trim();
            // String playerMove = playerMoves[i];

            String result = playRound(playerMove, computerMove);

            if (result.equals("Player Wins"))
                wins++;
            else if (result.equals("Computer Wins"))
                losses++;
            else
                draws++;

            roundNumbers[i] = "Round " + (i + 1);
            playerChoices[i] = playerMove;
            computerChoices[i] = computerMove;
            results[i] = result;
        }

        // Print summary table
        System.out.printf("%-10s %-15s %-15s %-15s%n", "Round", "Player Move", "Computer Move", "Result");
        for (int i = 0; i < N; i++) {
            System.out.printf("%-10s %-15s %-15s %-15s%n",
                    roundNumbers[i], playerChoices[i], computerChoices[i], results[i]);
        }

        double winPercentage = (wins * 100.0) / N;
        System.out.println();
        System.out.println("Final Summary:");
        System.out.println("Wins: " + wins + " | Losses: " + losses + " | Draws: " + draws
                + " | Win % = " + winPercentage + "%");

        sc.close();
    }

    static String playRound(String playerMove, String computerMove) {
        if (playerMove.equalsIgnoreCase(computerMove)) {
            return "Draw";
        }

        if ((playerMove.equalsIgnoreCase("Rock") && computerMove.equalsIgnoreCase("Scissors")) ||
                (playerMove.equalsIgnoreCase("Paper") && computerMove.equalsIgnoreCase("Rock")) ||
                (playerMove.equalsIgnoreCase("Scissors") && computerMove.equalsIgnoreCase("Paper"))) {
            return "Player Wins";
        }

        return "Computer Wins";
    }
}