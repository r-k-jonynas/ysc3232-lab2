import java.util.Scanner;

/*
    A class for open-ended question.
    Takes care of the functionality for creating, saving as XML, answering, and grading of the question.
 */
public class OpenQuestion extends Question {
    String questionText;
    String answerText;
    boolean answered;
    private boolean marked;
    private boolean answeredCorrectly;

    OpenQuestion (String questionText) {
        this.questionText = questionText;
        this.marked = false;
        this.answered = false;
        this.answeredCorrectly = false;
    }

    /*
    Prints the open-ended question text.
     */
    @Override
    public void printQuestion() {
        System.out.println(questionText);
    }

    /*
    Creates user interface for answering a question.
    */
    @Override
    public void answerQuestion(Scanner sc) {
        System.out.println("Enter your answer here:");
        this.answerText = sc.nextLine();
        this.answered = true;
    }

    /*
    A function to mark the answer to the open-ended question and save the grade for this question.
     */
    public void markQuestion(Boolean isCorrect) {
        this.answeredCorrectly = isCorrect;
        this.marked = true;
    }

    /*
        Returns marks received.
    */
    @Override
    public int getPointsAwarded() {
        if (!marked) {
            throw new IllegalStateException();
        } else {
            int marks = this.answeredCorrectly ? 1 : 0;
            return marks;
        }
    }

    /*
    Generates XML string from the Question object
     */
    @Override
    public String toXML() {
        return "<question type='Open' " + "questionText='" + questionText.replace("'", "\\'") + "'/>\n ";
    }

    public static void main(String[] args) {
        OpenQuestion test1 = new OpenQuestion("What is good about Java?");
        System.out.println(test1.toXML());
        test1.printQuestion();
        Scanner scanner = new Scanner(System.in); // Create a Scanner object to direct stdin
        test1.answerQuestion(scanner);
//        System.out.println(test1.getPointsAwarded()); // Will throw IllegalStateException, not yet marked
        test1.markQuestion(true);
        System.out.println(test1.getPointsAwarded());
        test1.answerQuestion(scanner);
        test1.markQuestion(false);
        System.out.println(test1.getPointsAwarded());
        scanner.close();

    }

}
