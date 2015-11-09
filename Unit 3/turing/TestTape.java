package turing;

//A test program for the Tape class that calls most of the methods in that class.
//
//The output from this program should be:  Tape Contents:  Hello World
//                                       Final position at the W

public class TestTape {
	
	public static void main(String[] args) {
		
		Tape tape = new Tape();
		for (int i = 0; i < "World".length(); i++) {
			tape.setContent("World".charAt(i));
			tape.moveRight();
		}
		for (int i = 0; i < "Hello World".length(); i++)
			tape.moveLeft();
		for (int i =0; i < "Hello".length(); i++) {
			tape.setContent("Hello".charAt(i));
			tape.moveRight();
		}
		System.out.println("Tape Contents:  " + tape.getTapeContents());
		tape.moveRight();
		System.out.println("Final position at the " + tape.getContent());
	}

}