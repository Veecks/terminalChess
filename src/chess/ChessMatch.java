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
	private boolean checkMate;

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

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Invalid movement, you are in check!");
		}
		check = testCheck(opponentColor(currentPlayer)) ? true : false;
		
		if (testCheckMate(opponentColor(currentPlayer))) {
			checkMate = true;
		}

		nextTurn();
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

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece captured) {
		Piece piece = board.removePiece(target);
		((ChessPiece)piece).increaseMoveCount();
		board.placePiece(piece, source);

		if (captured != null) {
			capturedPieces.remove(captured);
			activePieces.add(captured);
			board.placePiece(captured, target);
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

		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));

		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
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

	private boolean testCheck(Color color) {
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
