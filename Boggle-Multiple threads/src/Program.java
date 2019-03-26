import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Program {
	public static void main(String[] args) {
		System.out.println("Multi-thread boggle");
		
		long startTime = System.currentTimeMillis();
		
		WordList wordlist = new WordList("Wordlist.txt", 1, 100);
		Board board = new Board(wordlist, 25);
		System.out.println(board.toString());
		ArrayList<String> words = board.find();
		Collections.sort(words,  new WordComparator());
		printWords(words);
//		System.out.println(words.size());
//		System.out.println(words);
//		try {
//			printWords(words, new PrintStream(new File("words.txt")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(words);
//		System.out.println(words.size());
		
		long endTime = System.currentTimeMillis();
//		System.out.println("time taken = " + (endTime - startTime));
		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.print("\nExecution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
	}
	private static void printWords(ArrayList<String> list) {
		System.out.println("Found " + list.size() + " word(s)" + " \n");
		int letters = list.get(0).length();
		System.out.println(letters + " letter words");
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).length() == letters) {
				System.out.println(list.get(i).toUpperCase());
			} else {
				System.out.println();
				letters = list.get(i).length();
				System.out.println(letters + " letter words");
				System.out.println(list.get(i).toUpperCase());
			}
		}
	}
	private static void printWords(ArrayList<String> list, PrintStream out) {
		out.println("Found " + list.size() + " word(s)" + " \n");
		int letters = list.get(0).length();
		out.println(letters + " letter words");
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).length() == letters) {
				out.println(list.get(i).toUpperCase());
			} else {
				out.println();
				letters = list.get(i).length();
				out.println(letters + " letter words");
				out.println(list.get(i).toUpperCase());
			}
		}
	}
}
