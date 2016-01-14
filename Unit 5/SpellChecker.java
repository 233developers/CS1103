package spellchecker;

import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;

/**
 * A program that acts as a spell checker for a text file of English words.
 * 
 * The program prompts the user for a text file of properly spelled words
 * to use as a dictionary for spell checking purposes.  Next, the user is
 * prompted for a text file to spell check.  The words in both text files
 * should be separated by one or more non-letter characters.  
 * <p>
 * If a word in the spell checked file is not found in the dictionary, the
 * program provides suggestions for possible correct spellings.
 * 
 * @author Ryan Coon
 */
public class SpellChecker {

	/**
	 * Prompts the user for file inputs and calls the
	 * routines for spell checking and suggestion output.
	 * 
	 * @param args Command line args (not used).
	 */
	public static void main(String[] args) {
		
		/*
		 * Store the dictionary words in a HashSet for maximum
		 * efficiency, but declare the reference variable as the
		 * interface type instead of a concrete type.
		 * (i.e. Collection<> dictionary vs HashSet<> dictionary)
		 * This allows the dictionary variable to be treated as
		 * a generic Collection as it is passed around a program,
		 * rather than the more specific HashSet that it is
		 * actually implemented as, which provides the
		 * flexibility to change its underlying implementation
		 * while using good encapsulation with loose coupling.
		 * I've tried to use similar practices throughout this
		 * program.
		 */
		Collection<String> dictionary = new HashSet<>();
		
		System.out.println("Choose the file to be used as a dictionary.\n");
		dictionary = createDictionary();
		
		if (dictionary.size() == 0) {
			System.out.println("No dictionary from which to perform spell "
				+ "checking operations.  Goodbye.");
			System.exit(0);
		}
		
		System.out.println("Choose the file to spell check.\n");
		
		File file = getInputFileNameFromUser("Select File to spell check");
		
		if (file != null) {
			System.out.println("Below is the list of potentially misspelled "
				+ "words and some possible correct spellings:\n");
			spellCheckWordsFromFile(file, dictionary);
		} else {
			System.out.println("No file selected to spell check.  Goodbye.");
		}
	}
	
	/**
	 * Obtains a user-selected file to be implemented as a dictionary.
	 * The words in the dictionary file should be separated by one or more
	 * non-letter characters.
	 * 
	 * @return The dictionary that has been filled with words
	 *     from the user's selected file.
	 */
	private static Collection<String> createDictionary()  {
		
		Collection<String> dict = new HashSet<>();
		
		File file = getInputFileNameFromUser("Select Dictionary File");
		
		if (file != null) {
			try {
				Scanner filein = new Scanner(file);
				
				while (filein.hasNext()) {
					String word = filein.next();
					word = word.toLowerCase();
					dict.add(word);
				}		
				filein.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Can't find dictionary file.  No words "
					+ "added to dictionary.");
			}
		}
		
		return dict;
	}
	
	/**
	 * Use <code>dict</code> to compare against words from the file.
	 * 
	 * The words in the file to be spell checked should be separated
	 * by one or more non-letter characters.  If a word is not in the
	 * dictionary supplied, possible correct suggestions are printed
	 * to standard output for each word in the input that does not
	 * match a word in the dictionary.
	 * 
	 * @param f The file object that has been checked to ensure it
	 *     is not null.
	 * @param dict The dictionary to use as the correct spellings
	 *     of English words.
	 */
	private static void spellCheckWordsFromFile(File f, Collection<String> dict) {
		
		/* 
		 * Store words that have already been output in a set so
		 * the program will not output the same misspelled word
		 * more than once.
		 */
		Collection<String> wordsToOutput = new HashSet<>();
		
		try {
			Scanner in = new Scanner(f);
			in.useDelimiter("[^a-zA-Z]+");
			
			while (in.hasNext()) {
				String word = in.next();
				word = word.toLowerCase();
				
				if (!dict.contains(word)) {
					if (!wordsToOutput.contains(word)) {
						outputSuggestions(word, dict);
						wordsToOutput.add(word);
					}
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file to spell check words.");
		}
	}
	
	/**
	 * Prints potentially misspelled words along with possible corrections.
	 * 
	 * @param badWord The word that is potentially misspelled.
	 * @param dict The dictionary to use as the correct spellings
	 *     of English words.
	 */
	private static void outputSuggestions(String badWord, Collection<String> dict) {
		
		/*
		 * Need to declare suggestions as a reference variable of type
		 * TreeSet in order to use TreeSet-specific methods.
		 */
		TreeSet<String> suggestions = new TreeSet<>();
		
		/*
		 * Trying to reduce the size and narrow down the responsibilities
		 * of this functionality by splitting this into a second routine.
		 * Add all the potential correct spellings for each word into
		 * suggestions.
		 */
		suggestions.addAll(corrections(badWord, dict));
		
		if (suggestions.size() == 0) {
			System.out.println(badWord + ": (no suggestions)");
		} else {
			
			/*
			 * Adapted Eck's code snippet from 10.4.2 to properly
			 * print elements separated with commas from a TreeSet
			 * of Strings.
			 */
			System.out.print(badWord + ": ");
			String firstWord = suggestions.first();  
			System.out.print(firstWord);
			for (String word : suggestions.tailSet(firstWord, false)) {
				System.out.print(", " + word);
			}
			System.out.println();
		}
	}
	
	/**
	 * Collects all the possible corrections to a misspelled word.
	 * 
	 * @param badWord The word that is potentially misspelled.
	 * @param dict The dictionary to use as the correct spellings
	 *     of English words.
	 * @return The collection of possible corrections to the
	 *     misspelled word.
	 */
	private static Collection<String> corrections(String badWord, Collection<String> dict) {
		
		Collection<String> corrections = new TreeSet<>();
		
		// Variables to hold the five possible corrections the program considers.
		String deletedLetters;
		String changedLetters;
		String insertedLetters;
		String swappedLetters;
		String spaceInserted;
		
		for (int i = 0; i < badWord.length(); i++) {
			
			/*
			 * Test each character of badWord with i and i + 1
			 * swapped.  Stops before length() - 1 to avoid an
			 * ArrayIndexOutOfBoundsException.
			 */
			if (i < badWord.length() - 1) {
				char[] c = badWord.toCharArray();
				char temp = c[i];
				c[i] = c[i + 1];
				c[i + 1] = temp;
				
				swappedLetters = new String(c);
				
				if (dict.contains(swappedLetters)) {
					corrections.add(swappedLetters);
				}
			}
			
			/*
			 * Check each position that could have a space inserted, testing if the two
			 * separate words are in the dictionary.
			 */
			if (dict.contains(badWord.substring(0,  i)) && dict.contains(badWord.substring(i))) {
				spaceInserted = badWord.substring(0, i) + ' ' + badWord.substring(i);
				corrections.add(spaceInserted);
			}
			
			/*
			 * Check each position with all of the letters a-z for letter deletions,
			 * changes, and insertions.
			 */
			for (char ch = 'a'; ch <= 'z'; ch++) {
				
				deletedLetters = badWord.substring(0, i) + badWord.substring(i + 1);
				if (dict.contains(deletedLetters)) {
					corrections.add(deletedLetters);
				}
				
				changedLetters = badWord.substring(0, i) + ch + badWord.substring(i + 1);
				if (dict.contains(changedLetters)) {
					corrections.add(changedLetters);
				}
				
				insertedLetters = badWord.substring(0, i) + ch + badWord.substring(i);
				if (dict.contains(insertedLetters)) {
					corrections.add(insertedLetters);
					
				// Also need to check adding each letter to end of badWord.
				} else if (dict.contains(badWord + ch)) {
					corrections.add(badWord + ch);
				}
			}
		}
		
		return corrections;
	}
	
    /**
     * Lets the user select an input file using a standard file
     * selection dialog box.  If the user cancels the dialog
     * without selecting a file, the return value is null.
     * 
     * @param title The title of the file dialog.
     * @return The user's file or null if the dialog is cancelled
     *     or closed.
     */
    private static File getInputFileNameFromUser(String title) {
       JFileChooser fileDialog = new JFileChooser();
       fileDialog.setDialogTitle(title);
       int option = fileDialog.showOpenDialog(null);
       if (option != JFileChooser.APPROVE_OPTION)
          return null;
       else
          return fileDialog.getSelectedFile();
    }
}