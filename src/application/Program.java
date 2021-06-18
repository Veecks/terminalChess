package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		System.out.println("Hello Chess!");
		
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> capturedList = new ArrayList<>();
		
		mainLoop: while(true) {
			try{
				clearConsole();
				UI.printChessMatch(chessMatch, capturedList);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				clearConsole();
				UI.printBoard(chessMatch.getPieces(),chessMatch.PossibleMovesOf(source));
				
				System.out.print("\nTarget: ");
				ChessPosition target = UI.readChessPosition(sc);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if(capturedPiece != null)
					capturedList.add(capturedPiece);
			}
			catch(RuntimeException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Press Enter to continue.");
				sc.nextLine();
				continue mainLoop;
			}
			finally{
				System.out.println();
			}
		}
	}
	
	// https://stackoverflow.com/questions/2979383/java-clear-the-consolepublic
	private final static void clearConsole()
	{
	    try
	    {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	        	System.out.print("\033[H\033[2J");

	    }
	    catch (final Exception e)
	    {
	        System.out.println("Error clearing the console: " + e.getMessage());
	    }
	}


}
