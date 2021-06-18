package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		System.out.println("Hello Chess!");
		
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> capturedList = new ArrayList<>();
		
		mainLoop: while(!chessMatch.getCheckMate()) {
			try{
				UI.clearConsole();
				UI.printChessMatch(chessMatch, capturedList);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				UI.clearConsole();
				UI.printBoard(chessMatch.getPieces(),chessMatch.PossibleMovesOf(source));
				
				System.out.print("\nTarget: ");
				ChessPosition target = UI.readChessPosition(sc);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if(capturedPiece != null)
					capturedList.add(capturedPiece);
			}
			catch(ChessException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Press Enter to continue.");
				sc.nextLine();
				continue mainLoop;
			}catch(InputMismatchException e) {
				System.out.println("InputError: " + e.getLocalizedMessage());
				System.out.println("Press Enter to continue.");
				sc.nextLine();
			}
			catch(RuntimeException e) {
				System.out.println("InputError: " + e.getLocalizedMessage());
				System.out.println("Press Enter to continue.");
				sc.nextLine();
			}
			finally{
				System.out.println();
			}
		}
		UI.clearConsole();
		UI.printChessMatch(chessMatch, capturedList);
	}
}
