import java.util.Scanner;

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

    @Override
    public void printQuestion() {
        System.out.println(questionText);
    }

    @Override
    public void answerQuestion(Scanner sc) {
        System.out.println("Enter your answer here:");
        this.answerText = sc.nextLine();
        this.answered = true;
    }

    public void markQuestion(Boolean isCorrect) {
        this.answeredCorrectly = isCorrect;
        this.marked = true;
    }

    @Override
    public int getPointsAwarded() {
        if (!marked) {
            throw new IllegalStateException();
        } else {
            int marks = this.answeredCorrectly ? 1 : 0;
            return marks;
        }
    }

    @Override
    public String toXML() {
//        return "<task name='" + name.replace("'", "\\'") + "' start='" +
//                start.toString() + "' duration='" + duration.toString() + "' deadline='" +
//                deadline.toString() + "' description='" + description.replace("'", "\\'") + "'/>\n ";

        return "<question type='Open' " + "questionText='" + questionText.replace("'", "\\'") + "'/>\n ";
    }

    public static void main(String[] args) {
        OpenQuestion test1 = new OpenQuestion("What is good about Java?");
        System.out.println(test1.toXML());
//        test1.printQuestion();
//        Scanner scanner = new Scanner(System.in); // Create a Scanner object to direct stdin
//        test1.answerQuestion(scanner);
////        System.out.println(test1.getPointsAwarded()); // Will throw IllegalStateException, not yet marked
//        test1.markQuestion(true);
//        System.out.println(test1.getPointsAwarded());
//        test1.answerQuestion(scanner);
//        test1.markQuestion(false);
//        System.out.println(test1.getPointsAwarded());
//        scanner.close();

    }

}
