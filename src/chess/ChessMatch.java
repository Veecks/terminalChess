package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

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
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column,row).toPosition());
	}
	
	private void initialSetup() {
		placeNewPiece('a',6,new Rook(board, Color.WHITE));
		placeNewPiece('b',4,new King(board, Color.WHITE));
		placeNewPiece('f',2,new King(board, Color.BLACK));
	}
}
