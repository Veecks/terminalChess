package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
	private Board board;
//	private int turn;
//	private Color currentPlayer;
//	private boolean check;
//	private boolean checkMate;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] aux = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				aux[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return aux;
	}
	
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(1,1));
		board.placePiece(new King(board, Color.WHITE), new Position(5,1));
		board.placePiece(new King(board, Color.BLACK), new Position(3,6));
	}
}
