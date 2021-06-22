package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<Piece> activePieces = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;

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

	public boolean getCheckMate() {
		return checkMate;
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ChessPiece enPassantVulnerable() {
		return enPassantVulnerable;
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
		
		Piece capturedPiece = null;
		capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Invalid movement due to check!");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		check = testCheck(opponentColor(currentPlayer)) ? true : false;
		if (testCheckMate(opponentColor(currentPlayer))) {
			checkMate = true;
		}
		nextTurn();
		
		//enPassant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2))
			enPassantVulnerable = movedPiece;
		else
			enPassantVulnerable = null;
		
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		((ChessPiece)p).increaseMoveCount();
		Piece capturedPiece = (board.thereIsAPiece(target)) ? board.removePiece(target) : null;
		board.placePiece(p, target);

		if (capturedPiece != null) {
			activePieces.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//king side castling
		if(p instanceof King && target.getColumn() - 2 == source.getColumn()) {
			Position rookSource = new Position(target.getRow(), target.getColumn() + 1);
			Position rookTarget = new Position(target.getRow(), target.getColumn() - 1);
			makeMove(rookSource, rookTarget);
			
		}
		
		//queen side castling
		if(p instanceof King && target.getColumn() + 2 == source.getColumn()) {
			Position rookSource = new Position(target.getRow(), target.getColumn() - 2);
			Position rookTarget = new Position(target.getRow(), target.getColumn() + 1);
			makeMove(rookSource, rookTarget);
		}
		
		//enPassant
		if(p instanceof Pawn && capturedPiece == null && source.getColumn() != target.getColumn()) {
			int i = (((ChessPiece)p).getColor() == Color.WHITE) ? 1 : -1;
			capturedPiece = board.removePiece(new Position(target.getRow() + i, target.getColumn()));
		}

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece captured) {
		Piece piece = board.removePiece(target);
		((ChessPiece)piece).decreaseMoveCount();
		board.placePiece(piece, source);

		if (captured != null) {
			capturedPieces.remove(captured);
			activePieces.add(captured);
			board.placePiece(captured, target);
		}
		
		//king side castling
		if(piece instanceof King && target.getColumn() - 2 == source.getColumn()) {
			Position rookSource = new Position(target.getRow(), target.getColumn() - 1);
			Position rookTarget = new Position(target.getRow(), target.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.piece(rookSource);
			makeMove(rookSource, rookTarget);
			rook.decreaseMoveCount();
			rook.decreaseMoveCount();
		}
		
		//queen side castling
		if(piece instanceof King && target.getColumn() + 2 == source.getColumn()) {
			Position rookSource = new Position(target.getRow(), target.getColumn() + 1);
			Position rookTarget = new Position(target.getRow(), target.getColumn() - 2);
			ChessPiece rook = (ChessPiece)board.piece(rookSource);
			makeMove(rookSource, rookTarget);
			rook.decreaseMoveCount();
			rook.decreaseMoveCount();
		}
		
		//enPassant
		int i = (currentPlayer == Color.WHITE) ? 1 : -1;
		if(piece instanceof Pawn && captured == enPassantVulnerable && target.getRow() != 3.5 - 0.5 * i) {
			board.removePiece(target);
			board.placePiece(captured, new Position(target.getRow() + i, target.getColumn()));
			capturedPieces.remove(captured);
			activePieces.add(captured);
		}
	}

	private void validateSourcePosition(Position pos) {
		if (!board.thereIsAPiece(pos))
			throw new ChessException("There is not a piece in source position.");
		if (!board.piece(pos).isThereAnyPossibleMove())
			throw new ChessException("This piece has no possible moves.");
		if (currentPlayer != ((ChessPiece) board.piece(pos)).getColor())
			throw new ChessException("It is not " + ((ChessPiece) board.piece(pos)).getColor() + "'s turn now!");
	}

	private void ValidateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target))
			throw new ChessException("This move is not possible.");
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		activePieces.add(piece);
	}

	private void initialSetup() {

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Horse(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Horse(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));

		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Horse(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Horse(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
	}

	private Color opponentColor(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = activePieces.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King)
				return (ChessPiece) p;
		}
		throw new IllegalStateException(color + " KING IS MISSING!");
	}

	public boolean testCheck(Color color) {
		Piece king = king(color);
		List<Piece> opponentPieces = activePieces.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponentColor(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			if (p.possibleMove(king.getPos()))
				return true;
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color))
			return false;
		List<Piece> list = activePieces.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());

		for (Piece p : list) {
			boolean[][] moves = p.possibleMoves();
			for (int i = 0; i < moves.length; i++) {
				for (int j = 0; j < moves.length; j++) {
					if (moves[i][j]) {
						Position source = p.getPos();
						Position target = new Position(i, j);
						
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						
						if(!testCheck)
							return false;
					}
				}
			}
		}
		return true;
	}
}
