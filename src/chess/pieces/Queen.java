package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		// above
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			if (!getBoard().positionExists(p))
				break;
			moves[p.getRow()][p.getColumn()] = (getBoard().piece(p) == null);
			p.setRow(p.getRow() - 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// down
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			if (!getBoard().positionExists(p))
				break;
			moves[p.getRow()][p.getColumn()] = (getBoard().piece(p) == null);
			p.setRow(p.getRow() + 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// left
		p.setValues(this.pos.getRow(), this.pos.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			if (!getBoard().positionExists(p))
				break;
			moves[p.getRow()][p.getColumn()] = (getBoard().piece(p) == null);
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// right
		p.setValues(this.pos.getRow(), this.pos.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			moves[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
		
		// up-right
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			moves[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
			p.setColumn(p.getColumn() + 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// down-right
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			moves[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
			p.setColumn(p.getColumn() + 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// down-left
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			moves[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);

		// up-left
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			moves[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExists(p))
			moves[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
		
		return moves;
	}
}
