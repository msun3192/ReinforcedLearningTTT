// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
import java.util.Stack;

public class Matchbox {
    private final Board board;
    private final int[] positions;
    private final int[] values;
    private final boolean[] playedBefore;
    public Matchbox(Board board){
        positions = new int[board.remain()];
        values = new int[board.remain()];
        playedBefore = new boolean[board.remain()];
        this.board = board;
        int count = 0;
        for (int i = 0; i < Board.N; i++) {
            if(board.isEmpty(i)) {
                positions[count] = i;
                values[count++] = 1;
            }
        }
    }
    public int getMove(){
        for (int i = 0; i < playedBefore.length; i++) {
            if(!playedBefore[i]){
                playedBefore[i] = true;
                return positions[i];
            }
        }
        return positions[Board.random(positions.length)];
    }
    public static void reward(Stack<Matchbox> matchboxes, Stack<Integer> moves){
        if(matchboxes.isEmpty()){
            return;
        }
        Matchbox lastMatchbox = matchboxes.pop();
        int lastMove = moves.pop();
        for (int i = 0; i < lastMatchbox.positions.length; i++) {
            if(lastMatchbox.positions[i] == lastMove){
                lastMatchbox.values[i] += 5;
            }
        }
        for (int value : lastMatchbox.values) {
            if (value < 2) {
                return;
            }
        }
        // if all the moves in this matchbox are good, reinforce the option of making this move from the parent
        reward(matchboxes,moves);
    }
    public static void punish(Stack<Matchbox> matchboxes, Stack<Integer> moves){
        Matchbox lastMatchbox = matchboxes.pop();
        int lastMove = moves.pop();
        for (int i = 0; i < lastMatchbox.positions.length; i++) {
            if(lastMatchbox.positions[i] == lastMove){
                lastMatchbox.values[i] --;
            }
        }
        for (int value : lastMatchbox.values) {
            if (value > 0) {
                return;
            }
        }
        // if all the moves in this matchbox are bad, remove the option of making this move from the parent
        punish(matchboxes,moves);
    }
    public int getBestMove(){
        int bestValue = -Integer.MIN_VALUE;
        int bestMove = 0;
        for (int i = 0; i < positions.length; i++) {
            if(values[i] > bestValue){
                bestValue = values[i];
                bestMove = positions[i];
            }
        }
        return bestMove;
    }

    public Board getBoard() {
        return board;
    }
}
