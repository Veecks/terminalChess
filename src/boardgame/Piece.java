package boardgame;

public abstract class Piece {
	protected Position pos;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	protected Board getBoard() {
		return board;
	}
	
	public abstract boolean[][] possibleMoves();
	
	public boolean possibleMove(Position pos) {
		return possibleMoves()[pos.getRow()][pos.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] moves = possibleMoves();
		for(int i = 0; i<moves.length; i++) {
			for(int j = 0; j<moves.length; j++) {
				if(moves[i][j] == true)
					return true;
			}
		}
		return false;
	}
}
