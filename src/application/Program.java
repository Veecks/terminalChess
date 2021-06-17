package application;

import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Hello Chess!");
		
		ChessMatch chessMatch = new ChessMatch();
		
		mainLoop: while(true) {
			try{
				UI.printBoard(chessMatch.getPieces());
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch(RuntimeException e) {
				System.out.println("Error: " + e.getMessage());
				continue mainLoop;
			}
			finally{
				System.out.println();
			}
		}
	}

}
