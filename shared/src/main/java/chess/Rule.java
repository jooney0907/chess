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
        ChessPiece movingPiece = board.getPiece(position);
        for(int[] direction: directions){
            int rowChange = direction[0];
            int columnChange = direction[1];
            int newRow = position.getRow() + rowChange;
            int newColumn = position.getColumn() + columnChange;
            while (inBoundary(newRow, newColumn)){
                ChessPosition newPosition = new ChessPosition(newRow, newColumn);
                ChessPiece pieceLocation = board.getPiece(newPosition);
                if(pieceLocation == null){
                    ChessMove move = new ChessMove(position,newPosition, null);
                    moves.add(move);
                } else {
                    if (pieceLocation.getTeamColor() == movingPiece.getTeamColor()) {
                        break;
                    }
                    else{
                    ChessMove move = new ChessMove(position, newPosition,null);
                    break;
                    }
                    }
                {
                }
                newRow += rowChange;
                newColumn += columnChange;
            }
        }

        return moves;
    }
}

