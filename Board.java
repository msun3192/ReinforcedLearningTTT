// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
import java.util.Arrays;
import java.util.Random;

public class Board {
    
    // -- Random ----------------------------------------------------------------------

    private static Random generator = new Random();

    public static void setSeed(int seed) {
        Board.generator = new Random(seed);
    }
    public static int random(int n) {
        // Returns a random integer between 0 (inclusive) and n (exclusive)
        return Board.generator.nextInt(n);
    }


    // -- Row & Column <--> Index -----------------------------------------------------

    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int N = ROWS * COLS;

    public static int index(int row, int column) {
        return COLS*row + column;
    }

    public static int row(int index) {
        return index / COLS;
    }

    public static int column(int index) {
        return index % COLS;
    }


    // -- Players ---------------------------------------------------------------------
    
    public static final char EMPTY = ' ';
    public static final char X = 'X';
    public static final char O = 'O';

    public static char firstPlayer() {
        return X;
    }

    public static char otherPlayer(char player) {
        return switch (player) {
            case X -> O;
            case O -> X;
            default -> throw new IllegalArgumentException("Invalid player: " + player);
        };
    }

    private static char player(char c) {
        return switch (c) {
            case 'X', 'x' -> X;
            case 'O', 'o' -> O;
            case '.', ' ' -> EMPTY;
            default -> throw new IllegalArgumentException("Invalid player: " + c);
        };
    }


    // -- Tic Tac Toe Board Representation --------------------------------------------

    private final char[] board; // A linear representation of the 3x3 array
    private int hash = 0;       // Cached for performance

    // Constructors

    public Board() { // Empty board constructor
        this.board = new char[N];
        Arrays.fill(this.board, EMPTY);
    }

    public Board(char[] value) {
        if (value.length != N) {
            throw new IllegalArgumentException("Wrong size board");
        }
        this.board = new char[N];
        for (int i = 0; i < N; i++) {
            this.board[i] = player(value[i]);
        }
    }

    public Board(String value) {
        if (value.length() != N) {
            throw new IllegalArgumentException("Wrong size board");
        }
        this.board = new char[N];
        for (int i = 0; i < N; i++) {
            this.board[i] = player(value.charAt(i));
        }
    }
        
    public Board(Board other) {  // Copy constructor
        this.board = Arrays.copyOf(other.board, N);
    }

    // Getters & Setters

    public char get(int index) {
        return this.board[index];
    }

    public char get(int row, int col) {
        return get(index(row, col));
    }

    public boolean isEmpty(int index) {
        return get(index) == EMPTY;
    }

    public boolean isEmpty(int row, int col) {
        return isEmpty(index(row, col));
    }

    public void set(int index, char value) {
        this.board[index] = player(value);
    }

    public void set(int row, int col, char value) {
        set(index(row, col), value);
    }

    // Moves

    public Board move(char player, int index) {
        assert isEmpty(index);
        assert player(player) != EMPTY;
        Board result = new Board(this);
        result.board[index] = player;
        return result;
    }

    public Board move(char player, int row, int col) {
        return move(player, index(row, col));
    }
        
    public int remain() {
        // Return the number of empty squares
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (this.isEmpty(i)) count++;
        }
        return count;
    }

    public int random() {
        // Return index of a random empty square
        int n = random(this.remain());
        int m = 0;
        for (int i = 0; i < N; i++) {
            if (!this.isEmpty(i)) continue;
            if (n == m++) return i;
        }
        return -1;
    }

    // Symmetry Transforms

    public Transform transform(Board other) {
        // Which transform is needed to map this board into the other board.
        for (Transform transform : Transform.values()) {
            if (this.equals(transform.apply(other))) return transform;
        }
        return null; // No such transform
    }

    // Wins

    private boolean wonAlongRow(char player, int row) {
        for (int col = 0; col < COLS; col++) {
            if (this.get(row, col) != player) return false;
        }
        return true;
    }

    private boolean wonAlongColumn(char player, int col) {
        for (int row = 0; row < ROWS; row++) {
            if (this.get(row, col) != player) return false;
        }
        return true;
    }

    private boolean wonAlongForwardDiagonal(char player) {
        for (int row = 0; row < ROWS; row++) {
            int col = row;
            if (this.get(row, col) != player) return false;
        }
        return true;
    }

    private boolean wonAlongReverseDiagonal(char player) {
        for (int row = 0; row < ROWS; row++) {
            int col = COLS - row - 1;
            if (this.get(row, col) != player) return false;
        }
        return true;
    }

    public boolean won(char player) {
        for (int row = 0; row < ROWS; row++) {
            if (this.wonAlongRow(player, row)) return true;
        }

        for (int col = 0; col < COLS; col++) {
            if (this.wonAlongColumn(player, col)) return true;
        }

        if (this.wonAlongForwardDiagonal(player)) return true;
        if (this.wonAlongReverseDiagonal(player)) return true;
        return false;
    }

    // Equality & Hashing

    public boolean equals(Board other) {
        assert (this.board.length == other.board.length);
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != other.board[i]) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Board && this.equals((Board) other);
    }

    @Override
    public int hashCode() {
        if (this.hash == 0) {
            for (int i = 0; i < N; i++) {
                switch (this.board[i]) {
                    case X:  this.hash = 4 * this.hash + 2; break;
                    case O:  this.hash = 4 * this.hash + 3; break;
                    default: this.hash = 4 * this.hash + 1; break;
                }
            }
        }
        return this.hash;
    }

    // Printing

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < this.board.length; i++) {
            char c = this.board[i];
            if (c == ' ') {
                result += '.';
            } else {
                result += c;
            }
        }
        return result;
    }

    public void print() {
        String rowSeparator = "";
        String colSeparator = "";
        for (int row = 0; row < ROWS; row++) {
            System.out.println(rowSeparator);
            colSeparator = " ";
            for (int col = 0; col < COLS; col++) {
                System.out.print(colSeparator);
                System.out.print(this.get(row, col));
                colSeparator = " | ";
            }
            System.out.println();
            rowSeparator = "---+---+---";
        }
    }
}
