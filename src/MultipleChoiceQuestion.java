import java.util.*;
;
/*
    A class for multiple-choice question.
    Takes care of the functionality for creating, saving as XML, answering, and grading of the question.
 */
public class MultipleChoiceQuestion extends Question {
    String questionText;
    Set<String> options;
    Set<String> studentsAnswers;
    private Set<String> correctOptions;
    private boolean answered;
    private int pointsAwarded;

    MultipleChoiceQuestion(String text, Set<String> allOptions, Set<String> correct) {
        this.questionText = text;
        this.options = allOptions;
        this.studentsAnswers = new HashSet<>();
        this.correctOptions = correct;
        this.answered = false;
    }

    /*
    Prints the multiple-choice question text and the answer options.
    */
    @Override
    public void printQuestion() {
        System.out.println(questionText);
        System.out.println("You can select from the following options:");
        for (String possibleAnswer:
             this.options) {
            System.out.println("---->  " + possibleAnswer);
        }
    }

    /*
        Creates user interface for answering a question, marks the answer and saves the grade for this question.
     */
    @Override
    public void answerQuestion(Scanner sc) {
        System.out.println("Enter your answer here:");
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.isEmpty()) {
                break;
            }
            this.studentsAnswers.add(line.trim());
        }

        this.answered = true;
        this.pointsAwarded = 0;
        for (String i: this.correctOptions) {
            if (this.studentsAnswers.contains(i)) {
                pointsAwarded++;
            }
        }
    }

    /*
    Returns marks received.
     */
    @Override
    public int getPointsAwarded() {
        return this.pointsAwarded;
    }

    /*
        Generates XML string from the Question object
    */
    @Override
    public String toXML() {
        String stringXML =  "<question type='MultipleChoice' " + "questionText='"
                + questionText.replace("'", "\\'") + "'> ";

        StringBuilder optionStr = new StringBuilder("\n");
        for (String opt:
                options) {
            optionStr.append("  <option" + " " + "name='")
                    .append(opt.replace("'", "\\'") + "' ")
                    .append("correctness='");
            if (correctOptions.contains(opt)) {
                optionStr.append("Correct");
            } else {
                optionStr.append("Incorrect");
            }
            optionStr.append("'> </option>\n");
        }

        stringXML += optionStr.toString();
        stringXML += "</question>";
        return stringXML;
    }

    public static void main(String[] args) {
        Set<String> answerOptions = new HashSet<>();
        answerOptions.add("Java");
        answerOptions.add("Python");
        answerOptions.add("C++");
        Set<String> correctOptions = new HashSet<>();
        correctOptions.add("Java");
        correctOptions.add("Python");
        String test1Text = "Which programming languages are good?";
        MultipleChoiceQuestion test1 = new MultipleChoiceQuestion(test1Text, answerOptions, correctOptions);
//        System.out.println(optionsToXML(test1.options));
        System.out.println(test1.toXML());

        Scanner scanner = new Scanner(System.in); // Create a Scanner object to direct stdin
        test1.answerQuestion(scanner);

        test1.answerQuestion(scanner);
        System.out.println(test1.getPointsAwarded());
        test1.answerQuestion(scanner);
        System.out.println(test1.getPointsAwarded());
        scanner.close();
    }

}
