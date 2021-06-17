package boardgame;

import chess.ChessException;

public class Board {
	private int rows;
	private int columns;
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if (rows < 1 && columns < 1)
			throw new BoardException("Its necessary to have at least 1 row and 1 collumn.");
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		return pieces[row][column];
	}

	public Piece piece(Position pos) {
		if(!positionExists(pos))
			throw new BoardException("Find Piece: Position doesnt exist.");
		return pieces[pos.getRow()][pos.getColumn()];
	}

	public void placePiece(Piece piece, Position pos) {
		if(thereIsAPiece(pos))
			throw new BoardException("Place piece: There is already a piece at position (" + pos + ")");
		pieces[pos.getRow()][pos.getColumn()] = piece;
		piece.pos = pos;
	}
	
	public Piece removePiece(Position pos) {
		if(!thereIsAPiece(pos))
			throw new BoardException("Remove: There is not a piece in this position.");
		Piece aux = piece(pos);
		aux.pos = null;
		pieces[pos.getRow()][pos.getColumn()] = null;
		return aux;
	}
	
	//booleans
	public boolean positionExists(Position pos) {
		return positionExists(pos.getRow(), pos.getColumn());
	}

	private boolean positionExists(int row, int column) {
		return (row >= 0 && column >= 0 && row < rows && column < columns);
	}

	public boolean thereIsAPiece(Position pos) {
		if(!positionExists(pos))
			throw new ChessException("thereIsAPice: Position doesnt exists");
		return (piece(pos) != null);
	}
}