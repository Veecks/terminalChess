package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//up
		p.setValues(this.pos.getRow()+1, this.pos.getColumn());
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//up-right
		p.setValues(this.pos.getRow()+1, this.pos.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//right
		p.setValues(this.pos.getRow(), this.pos.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//down-right
		p.setValues(this.pos.getRow()-1, this.pos.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//down
		p.setValues(this.pos.getRow()-1, this.pos.getColumn());
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//down-left
		p.setValues(this.pos.getRow()-1, this.pos.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//left
		p.setValues(this.pos.getRow(), this.pos.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//up-left
		p.setValues(this.pos.getRow()+1, this.pos.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		return moves;
	}
	
	private boolean canMove(Position target) {
		ChessPiece targetPiece = (ChessPiece) getBoard().piece(target);
		return targetPiece == null || targetPiece.getColor() != this.getColor();
	}
}
