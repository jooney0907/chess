package chess;
import java.util. *;

public class Rule {
    private boolean repeat;
    private int[][] directions;

    public Rule(boolean repeat, int[][] directions) {
        this.repeat = repeat;
        this.directions = directions;
    }

    private boolean inBoundary(int row, int column){
        return row >= 1 && row <= 8 && column >= 1 && column <= 8;
    }
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new HashSet<>();
        return moves;
    }
}

