package unit1.mathquiz;

import textio.TextIO;
import java.util.Random;

/**
 * @author Ryan Coon
 *
 */
public class MathQuiz {

	private double score;
	private int firstTryCorrect;
	private int secondTryCorrect;
	
	private AdditionProblem addQuestion = new AdditionProblem();
	private SubtractionProblem subQuestion = new SubtractionProblem();
	private MultiplicationProblem multQuestion = new MultiplicationProblem();
	private DivisionProblem divQuestion = new DivisionProblem();
	private Random rGen = new Random();
	
	private static final int NUM_QUIZ_QUESTIONS = 10;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("What's your name?");
		String name = TextIO.getln();
		System.out.println("Pleased to meet you, " + name + ".\n");
		System.out.println("This will be a math quiz consisting of "
			+ NUM_QUIZ_QUESTIONS + " basic math questions.");
		
		MathQuiz quiz = new MathQuiz();
		quiz.askQuestions(NUM_QUIZ_QUESTIONS);
		quiz.displayScore();
	}
	
	private void askQuestions(int numQuestions) {
		
		for (int questionNum = 1; questionNum <= numQuestions; questionNum++) {
			int questionType = rGen.nextInt(4);
			askQuestion(questionType);
		}
	}
	
	private void askQuestion(int typeOfQuestion) {
		int correctAnswer;
		
		switch (typeOfQuestion) {
			case 0:
				System.out.println(addQuestion.getNextProblem());
				correctAnswer = addQuestion.getAnswer();
				break;
			case 1:
				System.out.println(subQuestion.getNextProblem());
				correctAnswer = subQuestion.getAnswer();
				break;
			case 2:
				System.out.println(multQuestion.getNextProblem());
				correctAnswer = multQuestion.getAnswer();
				break;
			case 3:
				System.out.println(divQuestion.getNextProblem());
				correctAnswer = divQuestion.getAnswer();
				break;
			default:
				System.out.println(addQuestion.getNextProblem());
				correctAnswer = addQuestion.getAnswer();
		}
		loopAndScoreGuesses(correctAnswer); 
	}
	
	private void loopAndScoreGuesses(int solution) {
		int numGuesses = 0;
		int userGuess;
		
		while (numGuesses < 2) {
			userGuess = TextIO.getInt();
			if (userGuess == solution) {
				System.out.println("That's correct!\n");
				if (numGuesses < 1) {
					firstTryCorrect++;
					score++;
				} else {
					secondTryCorrect++;
					score += 0.5;
				}
				break;
			} else {
				numGuesses++;
				System.out.println("That's incorrect."
					+ (numGuesses < 2 ? "  Try again." : "\n"));
			}
			if (numGuesses >= 2) {
				System.out.println("The correct answer is " + solution + ".\n");
			}
		}
	}
	
	private void displayScore() {
		System.out.println("Your score was " + score);
		System.out.println("You answered " + firstTryCorrect + "/"
			+ NUM_QUIZ_QUESTIONS + " questions correct on your first try.");
		System.out.println("You answered " + secondTryCorrect + "/"
			+ NUM_QUIZ_QUESTIONS + " questions correct on your second try.");
	}
}