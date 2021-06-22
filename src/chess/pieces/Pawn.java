package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	public Pawn(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//up or down?
		int i = (this.getColor() == Color.WHITE) ? -1 : 1;
		
		//vertical
		p.setValues(this.pos.getRow()+1*i, this.pos.getColumn());
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;

		//vertical + 1 ?
		if (this.getMoveCount() == 0)
			p.setValues(this.pos.getRow()+2*i, this.pos.getColumn());
			if(getBoard().positionExists(p) && canMove(p))
				moves[p.getRow()][p.getColumn()] = true;
			
		//diagonal right
		p.setValues(this.pos.getRow()+i, this.pos.getColumn()+i);
		if(getBoard().positionExists(p) && canAttack(p))
			moves[p.getRow()][p.getColumn()] = true;
			
		//diagonal left
		p.setValues(this.pos.getRow()+i, this.pos.getColumn()-i);
		if(getBoard().positionExists(p) && canAttack(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		return moves;
	}
	
	private boolean canMove(Position target) {
		ChessPiece targetPiece = (ChessPiece) getBoard().piece(target);
		return targetPiece == null;
	}
	
	private boolean canAttack(Position target) {
		ChessPiece targetPiece = (ChessPiece) getBoard().piece(target);
		return targetPiece != null && targetPiece.getColor() != this.getColor();
	}
}
