package chess;

import java.util. *;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (getPieceType() == PieceType.PAWN) {
            return pawnMove(board, myPosition);
        }
        Rule rule = switch (getPieceType()) {
            case BISHOP -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
            case ROOK   -> new Rule(true, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
            case KNIGHT -> new Rule(false, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2,-1},
                    {1,2},{1,-2},{-1,2},{-1,-2}});
            case QUEEN  -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1},
                    {1, 0}, {-1, 0}, {0, 1}, {0, -1}});
            case KING   -> new Rule(false, new int[][]{{1, -1}, {-1, 1}, {-1, -1},
                    {1, 1},{1,0},{0,1},{-1,0},{0,-1}});
            default -> null;
        };

        return rule.getMoves(board, myPosition);
        }

    private Collection<ChessMove> pawnMove(ChessBoard board, ChessPosition position) {
    Collection<ChessMove> moves = new HashSet<>();
    int direc;

    if(pieceColor == ChessGame.TeamColor.WHITE){
        direc = 1;
    } else{
        direc = -1;
    }
    ChessPosition forward = new ChessPosition(position.getRow() + direc, position.getColumn());
    if(board.getPiece(forward) == null){
        ChessMove move = new ChessMove(position, forward, null);
        moves.add(move);
    }
    if ((pieceColor== ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
    pieceColor== ChessGame.TeamColor.BLACK && position.getRow() == 7){
        ChessPosition goTwo = new ChessPosition(position.getRow() + (2*direc), position.getColumn());
        if(board.getPiece(forward) == null && board.getPiece(goTwo) == null){
            ChessMove move = new ChessMove(position, goTwo, null);
            moves.add(move);
        }
    }
    return moves;
}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor
                && type == piece.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(pieceColor, type);
    }

    }
