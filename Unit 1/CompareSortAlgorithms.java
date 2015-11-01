package unit1.compare_sort_algorithms;

import java.util.*;

/**
 * A program to compare the sort efficiency of insertion sort,
 * selection sort, and the Java built-in Arrays.sort method on
 * integer arrays.
 * 
 * Sample times:
 * 
 * Sort Method		Size_1000	Size_10000	Size_100000
 * 
 * Insertion sort     0.001		  0.058        5.812
 * Selection sort     0.002       0.095        9.813
 * Arrays.sort        0.001       0.004        0.017
 * 
 * @author  Ryan Coon
 */
public class CompareSortAlgorithms {

	private static final int ARRAY_SIZE = 10000;
	
	// Create a new random number generator.
	private static Random rGen = new Random();
	
	/**
	 * Sort the array A into increasing order.
	 * 
	 * @param A An integer array.
	 */
	private static void insertionSort(int[] A) {
	      
	    int itemsSorted; // Number of items that have been sorted so far.

        // Assume that items A[0], A[1], ... A[itemsSorted-1] 
        // have already been sorted.  Insert A[itemsSorted]
        // into the sorted part of the list.
	    for (itemsSorted = 1; itemsSorted < A.length; itemsSorted++) {
	         
	        int temp = A[itemsSorted];  // The item to be inserted.
	        int loc = itemsSorted - 1;  // Start at end of list.
	      
	        while (loc >= 0 && A[loc] > temp) {
	           A[loc + 1] = A[loc]; // Bump item from A[loc] up to loc+1.
	           loc = loc - 1;       // Go on to next location.
	        }
	      
	        A[loc + 1] = temp; // Put temp in last vacated space.
	    }
	}
	
	/**
	 * Sort A into increasing order, using selection sort.
	 * 
	 * @param A An integer array.
	 */
    private static void selectionSort(int[] A) {
	   
        // Find the largest item among A[0], A[1], ...,
        // A[lastPlace], and move it into position lastPlace 
        // by swapping it with the number that is currently 
        // in position lastPlace.
	    for (int lastPlace = A.length-1; lastPlace > 0; lastPlace--) {
	         
	        int maxLoc = 0;  // Location of largest item seen so far.
	      
	        for (int j = 1; j <= lastPlace; j++) {
	            if (A[j] > A[maxLoc]) {
	                // Since A[j] is bigger than the maximum we've seen
	                // so far, j is the new location of the maximum value
	                // we've seen so far.
	                maxLoc = j;  
	            }
	        }
	      
	        int temp = A[maxLoc];  // Swap largest item with A[lastPlace].
	        A[maxLoc] = A[lastPlace];
	        A[lastPlace] = temp;
	      
	    }  // end of for loop
	   
	}
	
    /**
     * Creates an integer array of ARRAY_SIZE length and fills it
     * with random positive integer values.  Makes two copies of
     * the filled array, timing and sorting each of the identical
     * arrays with insertionSort, selectionSort, and Arrays.sort().
     * The time it takes each sort method to complete sorting is
     * printed to standard output.
     * 
     * @param args Command line args (not used).
     */
	public static void main(String[] args) {
		int[] array = new int[ARRAY_SIZE];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = rGen.nextInt(Integer.MAX_VALUE);
		}
		
		int[] arrayCopy1 = Arrays.copyOf(array, array.length);
		int[] arrayCopy2 = Arrays.copyOf(array, array.length);
		
		long startTime = System.currentTimeMillis();
		insertionSort(array);
		long runTime = System.currentTimeMillis() - startTime;		
		System.out.println("Insertion sort run time: " + runTime/1000.0);
		
		startTime = System.currentTimeMillis();
		selectionSort(arrayCopy1);
		runTime = System.currentTimeMillis() - startTime;		
		System.out.println("Selection sort run time: " + runTime/1000.0);
		
		startTime = System.currentTimeMillis();
		Arrays.sort(arrayCopy2);
		runTime = System.currentTimeMillis() - startTime;		
		System.out.println("Built-in Arrays.sort run time: " + runTime/1000.0);
	}
}