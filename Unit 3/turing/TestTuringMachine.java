package turing;

//A test program for the TuringMachine class.  It creates three machines
//and runs them.  The output from the program indicates the expected behavior.

public class TestTuringMachine {

	public static void main(String[] args) {

		TuringMachine writeMachine = new TuringMachine();

		writeMachine.addRules( new Rule[] {  // writes Hello on the tape then halts
				new Rule(0,' ',1,'H',false),
				new Rule(1,' ',2,'e',false),
				new Rule(2,' ',3,'l',false),
				new Rule(3,' ',4,'l',false),
				new Rule(4,' ',-1,'o',false)
		});

		System.out.println("Running machine #1.  Output should be:  Hello");
		String writeMachineOutput = writeMachine.run(new Tape());
		System.out.println( "Actual output is:                       " + writeMachineOutput );

		TuringMachine badMachine = new TuringMachine();
		badMachine.addRules( new Rule[] {  // writes ERROR on the tape then fails
				new Rule(0,' ',1,'R',true),
				new Rule(1,' ',2,'O',true),
				new Rule(2,' ',3,'R',true),
				new Rule(3,' ',4,'R',true),
				new Rule(4,' ',5,'E',true) // no rule for state 5!
		});

		System.out.println("\nRunning machine #2.  Should throw an IllegalStateException.");
		try {
			badMachine.run( new Tape() );
			System.out.println("No Error was thrown.");
		}
		catch (IllegalStateException e) {
			System.out.println("Caught Illegal Argument Exception, with error message:");
			System.out.println(e.getMessage());
		}

		String input = "aababbbababbabbaba";  // a string of a's and b's for input to the copy machine
		Tape tape = new Tape();
		for (int i = 0; i < input.length(); i++) {
			tape.setContent(input.charAt(i));
			tape.moveRight();
		}
		tape.moveLeft();  // now, input is written on the tape, with the machine on the rightmost character

		TuringMachine copyMachine = new TuringMachine();  // copies a string of a's and b's;
		                                                  // machine must start on leftmost char in the string		
		copyMachine.addRules(new Rule[] {
				new Rule(0,'a',1,'x',true),  // rules for copying an a
				new Rule(1,'a',1,'a',true),
				new Rule(1,'b',1,'b',true),
				new Rule(1,' ',2,' ',true),
				new Rule(2,'a',2,'a',true),
				new Rule(2,'b',2,'b',true),
				new Rule(2,' ',3,'a',false),
				new Rule(3,'a',3,'a',false),
				new Rule(3,'b',3,'b',false),
				new Rule(3,' ',3,' ',false),
				new Rule(3,'x',0,'x',true),
				new Rule(3,'y',0,'y',true),
				
				new Rule(0,'b',4,'y',true),  // rules for copying a b
				new Rule(4,'a',4,'a',true),
				new Rule(4,'b',4,'b',true),
				new Rule(4,' ',5,' ',true),
				new Rule(5,'a',5,'a',true),
				new Rule(5,'b',5,'b',true),
				new Rule(5,' ',7,'b',false),
				new Rule(7,'a',7,'a',false),
				new Rule(7,'b',7,'b',false),
				new Rule(7,' ',7,' ',false),
				new Rule(7,'x',0,'x',true),
				new Rule(7,'y',0,'y',true),
				
				new Rule(0,' ',8,' ',false),  // rules that change x and y back to a and b, then halt
				new Rule(8,'x',8,'a',false),
				new Rule(8,'y',8,'b',false),
				new Rule(8,' ',-1,' ',true)
		});
		
		System.out.println("\nRunning machine #3.  Output should be: " + input + " " + input);
		String copyMachineOutput = copyMachine.run(tape);
		System.out.println("Actual output is:                      " + copyMachineOutput);

	}
}