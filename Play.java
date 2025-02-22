// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
import java.util.Scanner;

public class Play {

    private static final char X = Board.X;
    private static final char O = Board.O;


    private static int getMyMove(Board board) {
        // first find trained matchbox
        Matchbox matchbox = Train.findMatchbox(board);
        // now find best move
        int bestMove = matchbox.getBestMove();
        return board.transform(matchbox.getBoard()).map(bestMove);
    }

    private static class Redo extends Exception {}
    private static class Quit extends Exception {}

    private static int getCoordinate(String dimension) throws Redo, Quit {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Your move (" + dimension + "): ");
            String line = input.nextLine().trim();

            switch (line.toLowerCase()) {
                case "":
                case "redo":
                    System.out.println("Try again");
                    throw new Redo();

                case "quit":
                case "exit":
                    System.out.println("Quitting game");
                    throw new Quit();

                case "?":
                case "help":
                    System.out.println("Enter a number between 1 and 3 (inclusive");
                    System.out.println("Enter blank line or 'redo' to try again");
                    System.out.println("Enter 'quit' to abandon the game");
                    System.out.println();
                    throw new Redo();
            }

            try {
                int value = Integer.parseInt(line);
                if (value > 0 && value <= 3) return value-1;
                System.err.println("Invalid " + dimension + ": " + line);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + line);
            }
        }
    }

    private static int getYourMove(Board board) throws Quit {
        while (true) {
            try {
                int row = getCoordinate("row");
                int col = getCoordinate("column");
                int index = Board.index(row, col);
                if (board.isEmpty(index)) return index;
                System.err.println("Space is occupied; try again");
                System.err.println();
            } catch (Redo ignored) {
            }
        }
    }

    private static void displayMove(int index) {
        int row = Board.row(index);
        int col = Board.column(index);

        System.out.println("My move: row = " + row + ", column = " + col);
    }

    private static void displayBoard(Board board) {
        System.out.println();
        board.print();
        System.out.println();
    }

    public static void play(boolean first) {
        char computer = first ? X : O;
        char player = Board.firstPlayer();
        Board board = new Board();
        int move;

        try {
            do {
                if (player == computer) {
                    move = getMyMove(board);
                    displayMove(move);
                } else {
                    move = getYourMove(board);
                }

                board = board.move(player,  move);
                if (player == computer) {
                    displayBoard(board);
                }

                if (board.won(player)) {
                    if (player == computer) {
                        System.out.println("I won");
                    } else {
                        displayBoard(board);
                        System.out.println("You won");
                    }
                    return;
                }
                player = Board.otherPlayer(player);
            } while (board.remain() > 0);

        } catch (Quit e) {
            return;
        }

        displayBoard(board);
        System.out.println("Tie");
    }
}
