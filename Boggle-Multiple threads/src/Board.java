import java.util.ArrayList;
import java.util.Random;

import javax.naming.directory.AttributeModificationException;


public class Board {
	
	boolean f = false;
	
	private String[][] board = {
			{"t", "e", "a", "m"},
			{"s", "y", "f", "s"},
			{"p", "a", "t", "e"},
			{"g", "n", "i", "d"}
	};
	private WordList wordlist;
	
	public Board(WordList wordlist, int size) {
		this.wordlist = wordlist;
		if(!f) {
			board = new String[size][size];
			for(int i = 0; i < size; i++) {
				for(int a = 0; a < size; a++) {
					board[i][a] = this.getRandomLetter();
				}
			}
		}
	}
	
	public String toString() {
		String out = "";
		for(String[] sa : board) {
			for(String s : sa) out+= s.toUpperCase() + " ";
			out += "\n";
		}
		return out;
	}
	
	private String getRandomLetter() {
		
		Random r = new Random();
		
		int n = r.nextInt(wordlist.size());
		return "" + wordlist.get(n).charAt(r.nextInt(wordlist.get(n).length()));
		
//		return "" + wordlist.get(new Random().nextInt(wordlist.size())).charAt(new Random().nextInt(wordlist.get(new Random().nextInt(wordlist.size())).length()-1));
//		
//		
//		int num = new java.util.Random().nextInt(26)+97;
//		return "" + (char)num;
	}
	public String[][] getBoard() {
		return board;
	}
	public ArrayList<String> find() {
		ArrayList<String> words = new ArrayList<String>();
		boolean[][] path = new boolean[board.length][board.length];
		
		
//		find(words, "", 0, 0, path);
		
//		for(int x = 0; x < board.length; x++) {
//			for(int y = 0; y < board[0].length; y++) {
//				System.out.println("Find init at: (" + x + ", " + y+ ")");
//				find(words, "", x, y, cloneArray(path));
//			}
//		}
		
//		MyThread[] threads = new MyThread[board.length * board[0].length];
		
		ArrayList<MyThread> threads = new ArrayList<MyThread>();
		
//		int arnum = 0;
		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board[0].length; y++) {
				synchronized (this) {
//					System.out.println("Find at(" + x + ", " + y + ")");
//					MyThread t = new MyThread(this, words, "", x, y, cloneArray(path));
//					threads[arnum] = new MyThread(this, words, "", x, y, cloneArray(path));
					threads.add(new MyThread(this, words, "", x, y, cloneArray(path)));
//					arnum++;
				}
			}
		}
		
		long st = System.currentTimeMillis();
		
		boolean done = false;
		
		System.out.println("checking if done");
		
		int attemptNum = 0;
		
		do {
			done = checkIfDone(threads);
			try {
				Thread.sleep((long) 1);
			} catch(Exception e) {}
//			attemptNum++;
//			System.out.println("Check#: " + attemptNum);
		} while(!done);
		
		for(MyThread thread : threads) {
			thread.stop();
		}
		
		System.out.println("Time taken for threads is:" + (System.currentTimeMillis() - st));
		
		
		this.removeDuplicates(words);
		
		return words;
	}
	
	private boolean checkIfDone(MyThread[] thr) {
		
		for(MyThread t : thr) {
			if(!t.done) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkIfDone(ArrayList<MyThread> thr) {
		
		for(MyThread t : thr) {
			if(!t.done) return false;
		}
		
		return true;
	}
	
	
	private boolean hasPartial(String word) {
		for(int i = 0; i < wordlist.size(); i++) {
			String s = wordlist.get(i);
			try {
				if(s.substring(0, word.length()).equals(word.toUpperCase())) {
					return true;
				}
			} catch(StringIndexOutOfBoundsException e) {
				// do nothing
			}
		}
		return false;
	}
	
	public ArrayList<String> find(ArrayList<String> words, String word, int x, int y, boolean[][] path) {
//		Base cases
		if(word.length() > wordlist.getLongestWordLength()) return words;
		if(word.length() == wordlist.getLongestWordLength()) {
			if(wordlist.contains(word.toUpperCase())) words.add(word);
			return words;
		}
		
		word+= board[y][x];
		path[y][x] = true;
		
//		if(!word.equals("") && !hasPartial(word)) {
//			return words;
//		}
//		
		if(binarySearch(wordlist, word) == -1) return words;
		
		if(wordlist.contains(word.toUpperCase()) && !words.contains(word)) words.add(word);
		ArrayList<String> directions = this.availableDirections(path, x, y);
		if(directions.size() == 0) return words;
		
		for(String s : directions) {
			if(s.equals("NW")) this.find(words, word, x-1, y-1, this.cloneArray(path));
			if(s.equals("N")) this.find(words, word, x, y-1, cloneArray(path));
			if(s.equals("NE")) this.find(words, word, x+1, y-1, cloneArray(path));
			
			if(s.equals("E")) this.find(words, word, x+1, y, cloneArray(path));
			if(s.equals("W")) this.find(words, word, x-1, y, cloneArray(path));
			
			if(s.equals("SW")) this.find(words, word, x-1, y+1, cloneArray(path));
			if(s.equals("S")) this.find(words, word, x, y+1, cloneArray(path));
			if(s.equals("SE")) this.find(words, word, x+1, y+1, cloneArray(path));
		}
		return words;
	}
	
	private boolean[][] cloneArray(boolean[][] ar) {
		boolean[][] clone = new boolean[ar.length][ar[0].length];
		for(int x = 0; x < ar.length; x++) {
			for(int y = 0; y < ar[0].length; y++) {
				clone[x][y] = ar[x][y];
			}
		}
		return clone;
	}
	
	private ArrayList<String> availableDirections(boolean[][] path, int x, int y) {
		ArrayList<String> moves = new ArrayList<String>();
		
		if(y == 0) {
			if(!path[y+1][x]) {
				moves.add("S");
			}
			if(x > 0) {
				if(!path[y+1][x-1]) {
					moves.add("SW");
				}
				if(!path[y][x-1]) {
					moves.add("W");
				}
			}
			if(x < board.length-1) {
				if(!path[y+1][x+1]) {
					moves.add("SE");
				}
				if(!path[y][x+1]) {
					moves.add("E");
				}
			}
			return moves;
		}
		
		if(y == board.length-1) {
			if(!path[y-1][x]) {
				moves.add("N");
			}
			if(x > 0) {
				if(!path[y-1][x-1]) {
					moves.add("NW");
				}
				if(!path[y][x-1]) {
					moves.add("W");
				}
			}
			if(x < board.length-1) {
				if(!path[y-1][x+1]) {
					moves.add("NE");
				}
				if(!path[y][x+1]) {
					moves.add("E");
				}
			}
			return moves;
		}
		
		if(x == board.length-1 && (y == 0 || y == board.length-1)) {
			if(!path[y][x-1]) {
				moves.add("W");
			}
			if(y == board.length-1) {
				if(!path[y-1][x]) {
					moves.add("N");
				}
				if(!path[y-1][x-1]) {
					moves.add("NW");
				}
			}
			if(y == 0) {
				if(!path[y+1][x-1]) {
					moves.add("SW");
				}
				if(!path[y+1][x]) {
					moves.add("S");
				}
			}
			return moves;
		}
		
		if(x == board.length-1 && y > 0 && y < board.length-1) {
			if(!path[y-1][x]) {
				moves.add("N");
			}
			if(!path[y-1][x-1]) {
				moves.add("NW");
			}
			if(!path[y][x-1]) {
				moves.add("W");
			}
			if(!path[y+1][x-1]) {
				moves.add("SW");
			}
			if(!path[y+1][x]) {
				moves.add("S");
			}
			return moves;
		}
		
		if(x > 0 && y > 0 && x < board.length-1 && y < board.length-1) {
			if(!path[y][x+1]) {
				moves.add("E");
			}
			if(!path[y+1][x+1]) {
				moves.add("SE");
			}
			if(!path[y+1][x]) {
				moves.add("S");
			}
			if(!path[y-1][x+1]) {
				moves.add("NE");
			}
			if(!path[y-1][x]) {
				moves.add("N");
			}
			if(!path[y-1][x-1]) {
				moves.add("NW");
			}
			if(!path[y][x-1]) {
				moves.add("W");
			}
			if(!path[y+1][x-1]) {
				moves.add("SW");
			}
			return moves;
		}
		
		
		if(x == 0 & y == 0) {
			if(!path[y][x+1]) {
				moves.add("E");
			}
			if(!path[y+1][x+1]) {
				moves.add("SE");
			}
			if(!path[y+1][x]) {
				moves.add("S");
			}
		}
		
		return moves;
	}
	/*
	 * West = x-1
	 * East = x+1
	 * South = y+1
	 * North = y-1
	 */
	
	
	/*
	 *  x x x x
	 *  x x x x
	 *  x x ;x x
	 *  x x x x
	 */
	
	private void removeDuplicates2(ArrayList<String> list) {
		ArrayList<String> temp = new ArrayList<String>();
		boolean[] ar = new boolean[list.size()];
		for(int i = 0; i < ar.length; i++) ar[i] = true;
		for(int i = 0; i < list.size(); i++) {
			for(int a = 0; i < list.size()-1 && ar[i]; a++) {
				if(list.get(a).equals(list.get(a+1))) ar[a+1] = false;
			}
		}
		for(int i = 0;i < list.size(); i++)
			if(ar[i])
				temp.add(list.get(i));
		list.clear();
		list.addAll(temp);
	}
	
	private void removeDuplicates(ArrayList<String> list) {
	    ArrayList<String> temp = new ArrayList<String>();

	    for(int i = 0; i < list.size(); i++) {
	        if(temp.size() == 0 || !temp.get(temp.size() - 1).equals(list.get(i)))
	            temp.add(list.get(i));
	    }

	    list.clear();
	    list.addAll(temp);
	}
	public static int binarySearch(ArrayList<String> a, String word) {
        int low = 0;
        int high = a.size() - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
//            System.out.println("range is: " + low + "-" + high + " midpoint: " + mid);

//            if (a[mid].compareTo(x) < 0) {
            if(a.get(mid).charAt(0) == word.charAt(0) && a.get(mid).length() > word.length()) {
	            if(a.get(mid).substring(0, word.length()).compareTo(word) < 0) {
	                low = mid + 1;
	            } else if (a.get(mid).substring(0, word.length()).compareTo(word) > 0) {
	                high = mid - 1;
	            } else {
	                return mid;
	            }
            } else {
            	if(a.get(mid).compareTo(word) < 0) {
            		low = mid + 1;
            	}
            	else if(a.get(mid).compareTo(word) > 0) {
            		high = mid-1;
            	}
            	else if(a.get(mid).compareTo(word) == 0) {
            		return mid;
            	}
            }
        }
        
        if(a.get(a.size()-1).length() >= word.length() && a.get(a.size()-1).substring(0,word.length()).compareTo(word) == 0) return a.size()-1;

        return -1;
    }
}
