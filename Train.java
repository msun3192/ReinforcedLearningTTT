// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
import java.util.HashMap;
import java.util.Stack;

public class Train {
	public static HashMap<Board,Matchbox> matchboxes = new HashMap<>();

	public static void train(double rate, int games) {
		int gamesPlayed = 0;
		boolean first = true;
		double training = rate + 0.01; // adding basically nothing
		while(training/gamesPlayed > rate && gamesPlayed <= games){

			Stack<Matchbox> parents1 = new Stack<>();
			Stack<Integer> moves1 = new Stack<>();

			Stack<Matchbox> parents2 = new Stack<>();
			Stack<Integer> moves2 = new Stack<>();
			//create a new board state
			Board board = new Board();
			// play that board state
			// check neither players has won and that board is clear
			while(!board.won('X') && !board.won('O') && board.remain() > 0){
				if(first) {
					Matchbox matchbox1 = findMatchbox(board);
					parents1.push(matchbox1);
					int move1 = matchbox1.getMove(); // saves last move made by bot //TODO you have to transform it
					moves1.push(move1);
					// change move1 to transformation
					int transformedMove = board.transform(matchbox1.getBoard()).map(move1);
					board.set(transformedMove,'X');
					first = false;
				}else {
					Matchbox matchbox2 = findMatchbox(board);
					parents2.push(matchbox2);
					int move2 = matchbox2.getMove(); // saves last move made by bot
					moves2.push(move2);
					int transformedMove = board.transform(matchbox2.getBoard()).map(move2);
					board.set(transformedMove,'O');
					first = true;
				}
			}
			gamesPlayed++;
			if(gamesPlayed % 100_000 == 0){
				System.out.println("Game #" + gamesPlayed + " completed out of " + games);
			}
			// board is complete or a player has won
			if(board.won('X')){
				// reward player 1
				Matchbox.reward(parents1,moves1);
				//punish player 2
				Matchbox.punish(parents2,moves2);
				training++;
			} else if (board.won('O')) {
				// reward player 2
				Matchbox.reward(parents2,moves2);
				// punish player 1
				Matchbox.punish(parents1,moves1);
				training++;
			} // game is draw
		}
		// Play games against yourself for training purposes.
		// Continue until the training rate drops below the
		// specified rate.  To make sure that this does not
		// take too long, it should also be limited by the
		// maximum number of games that can be played in one
		// minute.
		System.out.println("Final Learning rate: " + training/gamesPlayed);
	}
	public static Matchbox findMatchbox(Board board){
		for(Transform transform: Transform.values()) {
			// apply transformation
			Board transformedBoard = transform.apply(board);
			Matchbox matchbox = matchboxes.get(transformedBoard);
			if(matchbox != null){
				return matchbox;
			}
		}
		// arrive here when there is no match.
		// have to make new matchbox and add to matchboxes array
		Board newBoard = new Board(board);
		Matchbox matchbox = new Matchbox(newBoard); // this is the problem
		matchboxes.put(newBoard,matchbox);
		return matchbox;
	}

}
