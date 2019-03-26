import java.util.ArrayList;

public class MyThread extends Thread {
	public MyThread(Board board, ArrayList<String> words, String word, int x, int y, boolean[][] path) {
		this.board = board;
		this.words = words;
		this.word = word;
		this.x = x;
		this.y = y;
		this.path = path;
		
		done = false;
		
		super.start();
		
	}
	
	public boolean done;
	
	private Board board;
	private ArrayList<String> words;
	private String word;
	private int x, y;
	private boolean[][] path;
	
	
	public void run() {
		board.find(words, word, x, y, path);
		done = true;
	}
}
