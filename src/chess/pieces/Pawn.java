package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	private ChessMatch match;
	
	public Pawn(Board board, Color color, ChessMatch match) {
		super(board, color);
		this.match = match;
	}
	
	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] moves = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//Black or White?
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
		
		//enPassant
		if(this.pos.getRow() == 3.5 + 0.5 * i) {
			Position left = new Position(this.pos.getRow(), this.pos.getColumn() - 1);
			if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == match.enPassantVulnerable())
				moves[left.getRow() + i][left.getColumn()] = true;

			Position right = new Position(this.pos.getRow(), this.pos.getColumn() + 1);
			if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == match.enPassantVulnerable())
				moves[right.getRow() + i][right.getColumn()] = true;
		}
		
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
