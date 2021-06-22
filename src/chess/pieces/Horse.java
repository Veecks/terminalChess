package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Horse extends ChessPiece{

	public Horse(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "H";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		//1
		p.setValues(this.pos.getRow()-2, this.pos.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//2
		p.setValues(this.pos.getRow()-1, this.pos.getColumn()+2);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//3
		p.setValues(this.pos.getRow()+1, this.pos.getColumn()+2);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//4
		p.setValues(this.pos.getRow()+2, this.pos.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//5
		p.setValues(this.pos.getRow()+1, this.pos.getColumn()-2);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//6
		p.setValues(this.pos.getRow()+2, this.pos.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//7
		p.setValues(this.pos.getRow()-1, this.pos.getColumn()-2);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		//8
		p.setValues(this.pos.getRow()-2, this.pos.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p))
			moves[p.getRow()][p.getColumn()] = true;
		
		return moves;
	}
	
	private boolean canMove(Position target) {
		ChessPiece targetPiece = (ChessPiece) getBoard().piece(target);
		return targetPiece == null || targetPiece.getColor() != this.getColor();
	}
		
}
