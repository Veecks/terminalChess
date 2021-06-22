package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch match;
	
	public King(Board board, Color color, ChessMatch match) {
		super(board, color);
		this.match = match;
	}

	@Override
	public String toString() {
		return "K";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];

		canCastle(moves);
		
		Position p = new Position(0, 0);

		// up
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn());
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// up-right
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// right
		p.setValues(this.pos.getRow(), this.pos.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// down-right
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// down
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn());
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// down-left
		p.setValues(this.pos.getRow() - 1, this.pos.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// left
		p.setValues(this.pos.getRow(), this.pos.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		// up-left
		p.setValues(this.pos.getRow() + 1, this.pos.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		return moves;
	}

	private boolean canMove(Position target) {
		ChessPiece targetPiece = (ChessPiece) getBoard().piece(target);
		return targetPiece == null || targetPiece.getColor() != this.getColor();
	}
	
	private void canCastle(boolean[][] moves) {
		Position pos1 = new Position(getPos().getRow(), getPos().getColumn() + 3);
		Position pos2 = new Position(getPos().getRow(), getPos().getColumn() - 4);
		
		
		if(getBoard().positionExists(pos1)) {
			ChessPiece rook = (ChessPiece)getBoard().piece(pos1);
			if(rook instanceof Rook && this.getMoveCount() == 0 && rook.getMoveCount() == 0 && !match.getCheck())
				moves[getPos().getRow()][getPos().getColumn() + 2] = true;
		}
		
		if(getBoard().positionExists(pos2)) {
			ChessPiece rook = (ChessPiece)getBoard().piece(pos2);
			if(getBoard().positionExists(pos2) && rook instanceof Rook && this.getMoveCount() == 0 && rook.getMoveCount() == 0 && !match.getCheck())
				moves[getPos().getRow()][getPos().getColumn() - 2] = true;
		}
	}
}
