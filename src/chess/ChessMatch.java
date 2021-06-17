package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
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
	
	public boolean[][] PossibleMovesOf(ChessPosition chessPos) {
		Position pos = chessPos.toPosition();
		validateSourcePosition(pos);
		return board.piece(pos).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourceCPos, ChessPosition targetCPos) {
		Position source = sourceCPos.toPosition();
		validateSourcePosition(source);
		Position target = targetCPos.toPosition();
		ValidateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = (board.thereIsAPiece(target)) ? board.removePiece(target) : null;
		board.placePiece(p, target);
		return capturedPiece;
	}

	private void validateSourcePosition(Position pos) {
		if(!board.thereIsAPiece(pos))
			throw new ChessException("There is not a piece in source position.");
		if(!board.piece(pos).isThereAnyPossibleMove())
			throw new ChessException("This piece has no possible moves.");
	}
	
	private void ValidateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target))
			throw new ChessException("This move is not possible.");
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
