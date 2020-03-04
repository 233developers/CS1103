package unit4;

import textio.TextIO;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * This program is a very simple "set calculator" that can compute
 * the intersection, union, and set difference of two sets of 
 * non-negative integers.  Each line of the user's input contains two 
 * such sets, separated by one of the operators +, *, or -, standing
 * for union, intersection, and set difference respectively.  A set
 * must be given in the form of a list of non-negative integers, separated
 * by commas, and enclosed in square brackets.  For example:
 * [1, 2, 3] + [4, 3, 10, 0].  Spaces can occur anywhere in the input.
 * If an error is found in the input, the program will report it.
 * The program ends when the user inputs an empty line.
 * 
 * @author Ryan Coon
 */
public class SetCalculator {

    /**
     * An object of type ParseError represents a syntax error found in 
     * the user's input.
     */
    private static class ParseError extends Exception {
        ParseError(String message) {
            super(message);
        }
    } // end nested class ParseError
    
    /**
     * Reads a set of non-negative integers from standard input, and
     * stores them in an ArrayList of Integers.  The set must be
     * enclosed between square brackets and must contain a list of
     * zero or more non-negative integers, separated by commas.  Spaces
     * are allowed anywhere.  If the input is not of the correct form,
     * a ParseError is thrown.
     * 
     * @return An ArrayList<Integer> containing all the Integers
     *     between the square brackets.
     * @throws ParseError If input is not of the correct form.
     */
    private static ArrayList<Integer> getSetElements() throws ParseError {
    	
    	ArrayList<Integer> setElements = new ArrayList<>();
    	
    	TextIO.skipBlanks();
    	if (TextIO.peek() != '[') {
    		throw new ParseError("Must begin the set with a left bracket.");
    	} else {
    		TextIO.getAnyChar();
    	}
    	
    	while (true) {
    		TextIO.skipBlanks();
    		char ch = TextIO.peek();
    		
    		if (ch == ']') {
    			TextIO.getAnyChar();
    			break;
    		} else if (ch == ',') {
    			TextIO.getAnyChar();
    		} else if ( ch == '\n' ) {
                throw new ParseError("End-of-line encountered before the end of the set.");
    		} else if (Character.isDigit(ch)) {
    			int num = TextIO.getInt();
    			setElements.add(num);
    		} else {
    			throw new ParseError("Unexpected character \"" + ch + "\" encountered.");
    		}
    	}
    	
    	return setElements;
    }
    
    /**
     * If the next character in input is one of the legal operators,
     * read it and return it.  Otherwise, throw a ParseError.
     */
    private static char getOperator() throws ParseError {
        TextIO.skipBlanks();
        char op = TextIO.peek(); 
        if ( op == '+' || op == '-' || op == '*') {
            TextIO.getAnyChar();
            return op;
        }
        else if (op == '\n')
            throw new ParseError("Missing operator before end of line.");
        else
            throw new ParseError("Missing operator.  Found \"" +
                    op + "\" instead of +, -, or *.");
    } // end getOperator()
    
    /**
     * Prints the results of the set operations.
     * 
     * @param op Operator character.  Should be +, *, or -.
     * @param setA TreeSet<Integer> for the first set.
     * @param setB TreeSet<Integer> for the second set.
     */
    private static void printSetResults(char op, 
    	TreeSet<Integer> setA, TreeSet<Integer> setB) {
    	
    	switch (op) {   //  Apply the operator and print the result. 
	    	case '+':
	    		System.out.print(setA + " + " + setB + " = ");
	    		setA.addAll(setB);
	    		System.out.println(setA);
	    		break;
	    	case '*':
	    		System.out.print(setA + " * " + setB + " = ");
	    		setA.retainAll(setB);
	    		System.out.println(setA);
	    		break;
	    	case '-':
	    		System.out.print(setA + " - " + setB + " = ");
	    		setA.removeAll(setB);
	    		System.out.println(setA);
	    		break;
	    	// Can't occur since op is one of the above.
	    	// (But Java syntax requires a return value.)
	    	default:
	    		System.out.println("No operator.");
	    }
    }
    
    /**
     * Read a line of input, consisting of two sets separated by
     * an operator.  Perform the operation and output the value.
     * If any syntax error is found in the input, a ParseError
     * is thrown.
     */
    private static void calculateSetOperation() throws ParseError {
        
    	TreeSet<Integer> setA = new TreeSet<>();
        setA.addAll(getSetElements());
        
        char op = getOperator();
        
        TreeSet<Integer> setB = new TreeSet<>();
        setB.addAll(getSetElements());
        
        printSetResults(op, setA, setB);
        TextIO.getln();
    }
    
    /**
     * Runs the loop to continue processing set calculations until
     * the user enters an empty line.
     * 
     * @param args Command line args (not used).
     */
	public static void main(String[] args) {
		
		System.out.println("This program works as a set calculator for "
			+ "non-negative integers.");
		System.out.println("Enter two sets of integers separated by "
			+ "commas and, optionally, spaces and enclosed in square "
			+ "brackets.  Ex: [1,2,3]");
		System.out.println("In between the two sets, use \"+\" to find "
			+ "the union of the two sets, \"*\" to find their intersection, "
			+ "and \"-\" to find the difference.");
		
		while (true) {
			System.out.println("\nEnter two sets separated by an operator,");
			System.out.println("or press return to end.");
			System.out.print("\n?  ");
            TextIO.skipBlanks();
            if ( TextIO.peek() == '\n' ) {
            	break;
            }
            
            try {
            	calculateSetOperation();  
            } catch (ParseError e) {
            	System.out.println("\n*** Error in input:    " + e.getMessage());
                System.out.println("*** Discarding input:  " + TextIO.getln());
            }
            
		}  // end while
		
		System.out.println("\n\nGoodbye.");
		
	}  // end main
	
}  // end class SetCalculator