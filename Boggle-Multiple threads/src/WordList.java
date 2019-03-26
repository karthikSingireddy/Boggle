import java.util.*;
import java.io.*;

public class WordList extends ArrayList<String>
{
    private static final long serialVersionUID = -1447871553241214941L;
    private int longestWordLength = 0;
    
    // read all the words from the specified file.  place
    // all the words of the specified sizes in  an  array
    // which is then maintained as a sorted list for fast
    // lookup
    
    public WordList(String filename, int min, int max) {

        try  {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            try
            {
            	
            	while (scanner.hasNext()) {
	                String word = scanner.next();
	                
	                // only use the words within specified size
	                
	                if (word.length() >= min && word.length() <= max) {
	                	
	                	// add words using super, so it doesn't
	                	// resort the list on every single  add
	                	
	                	super.add(word.toUpperCase());
	                	
	                	
	                	if (word.length() > this.longestWordLength) {
	                		this.longestWordLength = word.length();
	                	}
	                }
	            }
            }
            finally {
            	scanner.close();
            }
        }
        catch (Exception e) {
        	throw new RuntimeException("Error reading word list", e);
        }
        
        // sort the collection for  fast  lookup

        System.out.println("Loaded " + this.size() + " word(s)");
        System.out.println("Longest word " + this.longestWordLength);
        Collections.sort(this);
        
        // get rid of any duplicates in the list
        // do it backwards to simplify  removals 
        
        for (int i = this.size() - 2; i >= 0; i--) {
             if (this.get(i).equals(this.get(i + 1))) {
                 this.remove(i + 1);
             }
        }
    }

    // it is sorted. use binarySearch  for  speed

    @Override
    public boolean contains(Object o) {
        if (! (o instanceof String)) return false;
        return Collections.binarySearch(this, (String) o) >= 0;
    }
    
    // if someone wants to add after construction
    // then we need to  insert  in  sorted  order
    
    @Override
    public boolean add(String s) {
    	int index = Collections.binarySearch(this,  s);
    	if (index >= 0) return false;
    	
    	super.add(-index, s);
    	return true;
    }
    
    // if they want to add a bunch of stuff  then
    // let them,  but we don't  allow  duplicates
    
    
    public boolean addAll(Collection<? extends String> c)
    {
    	// if there are  any  duplicates  in  the
    	// added collection,  then we add nothing
    	
    	for (String s : c) {
    		if (this.contains(s)) {
    			throw new RuntimeException("Cannot add duplicate words.");
    		}
    	}
    	
    	// call the super to do the add, and then
    	// make sure  collection is still  sorted
    	
    	boolean result = super.addAll(c);
    	Collections.sort(this);
    	return result;
    }
    
    // insertion at an index is allowed provided
    // that the overall sort order is maintained 
    
    @Override
    public void add(int index, String s) {
    	if (Collections.binarySearch(this,  s) != -index)
        	super.add(-index, s);
        else {
			throw new RuntimeException("Adding " + s + " at position " + index + " would break the word order");
        }
    }
    
    public int getLongestWordLength() {
    	return this.longestWordLength;
    }
 
    public void Dump() {
        // display the full list in order without dups
        
        char previous = this.get(0).charAt(0);
        
        for (int i = 0; i < this.size(); i++) {
        	String word = this.get(i);
        	
        	if (word.charAt(0) != previous) {
        		System.out.println("\n");
        		previous = word.charAt(0);
        	}
        	
        	System.out.print(word + " ");
        }
        
        System.out.println();
    }
}
