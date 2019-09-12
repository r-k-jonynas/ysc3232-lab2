import java.util.Scanner;
/*
    A class for Yes-No question.
    Takes care of the functionality for creating, saving as XML, answering, and grading of the question.
    Provides functionality for custom names for Yes and No options.
 */
public class YesNoQuestion extends Question {
    private String questionText;
    private String YesOption;
    private String NoOption;
    private int correctOption;
    private boolean answered;
    private boolean answeredCorrectly;

    YesNoQuestion (String questionText, String yes, String no, int correct) {
        this.questionText = questionText;
        this.YesOption = yes;
        this.NoOption = no;
        this.correctOption = correct;
        this.answered = false;
        this.answeredCorrectly = false;
    }
    /*
        Prints the yes-no question text.
     */
    @Override
    public void printQuestion() {
        System.out.println(questionText);
    }

    /*
        Creates user interface for answering a question and grades the answer.
    */
    @Override
    public void answerQuestion(Scanner sc) {
        System.out.println("Select 1 for [" + YesOption + "] or 0 for [" + NoOption + "]:");
        int answer = 0;
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            // Make sure input isn't empty
            if (line.isEmpty()) {
                System.out.println("Empty input: Enter a valid input: 1 or 0:");
                continue;
            }
            // Trim the input
            line = line.trim();
            // Validate inputs
            if (line.equals("0") | line.equals("1")) {
                // Convert to integer.
                answer = Integer.parseInt(line);
                break;
            } else {
                System.out.println("Enter a valid input: 1 or 0:");
            }
        }
        System.out.println( "Your answer: <" + answer + "> recorded");

        // Update answered variable and grade
        this.answered = true;
        this.answeredCorrectly = (answer == this.correctOption);
    }

    /*
        Returns marks received.
    */
    @Override
    public int getPointsAwarded() {
        if (answered && answeredCorrectly) {
            return 1;
        } else {
            return 0;
        }
    }

    /*
    Generates XML string from the Question object
     */
    @Override
    public String toXML() {
        String stringXML =  "<question type='YesNoQuestion' " + "questionText='"
                + questionText.replace("'", "\\'") + "'>";

        StringBuilder yesNoStrBuilder = new StringBuilder("\n");
        String yesCorrectness, noCorrectness;
        if (correctOption == 1) {yesCorrectness = "Correct";  noCorrectness = "Incorrect";}
        else {yesCorrectness = "Incorrect";  noCorrectness = "Correct";}

        yesNoStrBuilder.append("  <option" + " " + "name='")
                .append(this.YesOption.replace("'", "\\'") + "' ")
                .append("optionType='Yes' ")
                .append("correctness='")
                .append(yesCorrectness.replace("'", "\\'") + "'> </option>\n");
        yesNoStrBuilder.append("  <option" + " " + "name='")
                .append(this.NoOption.replace("'", "\\'") + "' ")
                .append("optionType='No' ")
                .append("correctness='")
                .append(noCorrectness.replace("'", "\\'") + "'> </option>\n");


        stringXML += yesNoStrBuilder.toString();
        stringXML += "</question>";
        return stringXML;

    }
}
