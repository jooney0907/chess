package ui;

import chess.*;
import java.util.*;
import static ui.EscapeSequences.*;

public class PrintChessBoard {
    private final ChessGame.TeamColor color;
    private static final String LIGHT_SQUARE = "\u001B[48;2;240;229;229m";
    private static final String DARK_SQUARE = "\u001B[48;2;161;82;82m";
    private static final String COORDINATE_COLOR = "\u001B[38;2;161;82;82m";
    private static final String WHITE_PIECE_COLOR = "\u001B[38;2;0;45;150m";
    private static final String BLACK_PIECE_COLOR = "\u001B[38;2;140;0;20m";

    public PrintChessBoard(ChessGame.TeamColor color) {
        this.color = color;
    }

    public void print(ChessBoard board){
        boolean isWhite = color != ChessGame.TeamColor.BLACK;
        var output = new StringBuilder();
        String letters = isWhite
                ? "    a   b   c   d   e   f   g   h    "
                : "    h   g   f   e   d   c   b   a    ";
        output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                .append(letters).append(RESET_TEXT_COLOR).append("\n");
        for (int row = 0; row < 8; row++) {
            int rowNum = isWhite ? 8 - row : row + 1;
            output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                    .append(" ").append(rowNum).append(" ").append(RESET_TEXT_COLOR);
            for (int col = 0; col < 8; col++) {
                int colNum = isWhite ? col + 1 : 8 - col;
                if ((row + col) % 2 == 0) {
                    output.append(LIGHT_SQUARE);
                } else {
                    output.append(DARK_SQUARE);
                }
                ChessPosition position = new ChessPosition(rowNum, colNum);
                ChessPiece piece = board.getPiece(position);
                output.append(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE
                        ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);
                output.append(getPieceSymbol(piece));
            }
            output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                    .append(" ").append(rowNum).append(" ").append(RESET_TEXT_COLOR).append("\n");
        }
        output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                .append(letters).append(RESET_TEXT_COLOR).append(RESET_BG_COLOR);
        System.out.println(output);
    }

    public void highlightBoard(ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        Set<ChessPosition> highlights = new HashSet<>();
        if (board.getPiece(position) != null) {
            Collection<ChessMove> moves = game.validMoves(position);
            if (moves != null) {
                for (ChessMove move : moves) {
                    highlights.add(move.getEndPosition());
                }
            }
        }
        highlights.add(position);
        boolean isWhite = color != ChessGame.TeamColor.BLACK;
        StringBuilder output = new StringBuilder();

        String letters = isWhite
                ? "    a   b   c   d   e   f   g   h    "
                : "    h   g   f   e   d   c   b   a    ";
        output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                .append(letters).append(RESET_TEXT_COLOR).append("\n");
        for (int row = 0; row < 8; row++) {
            int rowNum = isWhite ? 8 - row : row + 1;
            output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                    .append(" ").append(rowNum).append(" ").append(RESET_TEXT_COLOR);
            for (int col = 0; col < 8; col++) {
                int colNum = isWhite ? col + 1 : 8 - col;
                ChessPosition square = new ChessPosition(rowNum, colNum);
                if (highlights.contains(square)) {
                    output.append(SET_BG_COLOR_GREEN);
                } else if ((row + col) % 2 == 0) {
                    output.append(LIGHT_SQUARE);
                } else {
                    output.append(DARK_SQUARE);
                }
                ChessPiece piece = board.getPiece(square);
                output.append(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE
                        ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);
                output.append(getPieceSymbol(board.getPiece(square)));
            }
            output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                    .append(" ").append(rowNum).append(" ").append(RESET_TEXT_COLOR).append("\n");
        }
        output.append(RESET_BG_COLOR).append(COORDINATE_COLOR)
                .append(letters).append(RESET_TEXT_COLOR).append(RESET_BG_COLOR);
        System.out.println(output);
    }

    private String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}