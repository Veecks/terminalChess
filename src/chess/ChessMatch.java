package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<Piece> activePieces = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private boolean check;
//	private boolean checkMate;

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false;
		initialSetup();
	}

	public boolean getCheck() {
		return check;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
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
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			System.out.println("NOOOOOO");
			throw new ChessException("Invalid movement: Check!");
		}
		
		check = testCheck(opponentColor(currentPlayer)) ? true : false;
		
		nextTurn();
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = (board.thereIsAPiece(target)) ? board.removePiece(target) : null;
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			activePieces.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
			
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece captured) {
		if(captured != null) {
			capturedPieces.remove(captured);
			activePieces.add(captured);
		}
		Piece piece = board.removePiece(target);
		board.placePiece(piece, source);
		board.placePiece(captured, target);
			
	}

	private void validateSourcePosition(Position pos) {
		if(!board.thereIsAPiece(pos))
			throw new ChessException("There is not a piece in source position.");
		if(!board.piece(pos).isThereAnyPossibleMove())
			throw new ChessException("This piece has no possible moves.");
		if(currentPlayer != ((ChessPiece)board.piece(pos)).getColor())
			throw new ChessException("It is not " + ((ChessPiece)board.piece(pos)).getColor() + "'s turn now!");
	}
	
	private void ValidateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target))
			throw new ChessException("This move is not possible.");
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		activePieces.add(piece);
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
	
	private Color opponentColor(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = activePieces.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King)
				return (ChessPiece)p;
		}
		throw new IllegalStateException(color + " KING IS MISSING!");
	}
	
	private boolean testCheck(Color color) {
		Piece king = king(color);
		List<Piece> opponentPieces = activePieces.stream().filter(x -> ((ChessPiece)x).getColor() == opponentColor(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			if(p.possibleMove(king.getPos()))
				return true;
		}
		return false;
	}
}
