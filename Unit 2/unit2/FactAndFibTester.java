package unit2;

import java.util.Scanner;

/**
 * A program to test recursive factorial and fibonacci functions.
 * 
 * @author Ryan Coon
 *
 */
public class FactAndFibTester {
	
	/**
	 * Calculates the factorial of num recursively.
	 * 
	 * Precondition: num is a non-negative integer.
	 * 
	 * @param num A non-negative integer.
	 * @return The factorial of num.
	 */
	private static int factorial(int num) {
		
		if (num == 0 || num == 1) {
			return 1;
		} else {
			return num * factorial(num - 1);
		}
	}
	
	/**
	 * Calculates the "num"th Fibonacci number recursively.
	 * 
	 * Precondition: num is a non-negative integer.
	 * 
	 * @param num A non-negative integer.
	 * @return The fibo of num.
	 */
	private static int fibonacci(int num) {
		
		if (num <= 1) {
			return num;
		} else {
			return fibonacci(num - 1) + fibonacci(num - 2);
		}
	}
	
	/**
	 * Obtains a non-negative integer from the user, passing it to
	 * the factorial and fibonacci functions and printing the results
	 * to standard output.
	 * 
	 * @param args Command line args (not used).
	 */
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int number = -1;
		String num = "";
		
		// Ensure number is a positive integer.
		while (true) {
			System.out.println("Enter a non-negative integer: ");
			try {
				num = sc.next();
				number = Integer.parseInt(num);
			} catch (NumberFormatException e) {
				System.out.println(num + " is not a parsable integer.\n");
			}
			if (number >= 0) {
				break;
			}
		}
		
		System.out.println("Factorial of " + number + " equals: "
			+ factorial(number));
		System.out.println("Fibonacci " + number + " equals: "
			+ fibonacci(number));
		sc.close();
	}
}