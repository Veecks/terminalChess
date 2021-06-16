package boardgame;

public class Piece {
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
}
