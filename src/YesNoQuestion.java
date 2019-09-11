import java.util.Scanner;

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

    @Override
    public void printQuestion() {
        System.out.println(questionText);
    }

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

    @Override
    public int getPointsAwarded() {
        if (answered && answeredCorrectly) {
            return 1;
        } else {
            return 0;
        }
    }

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

    public static void main(String[] args) {
        YesNoQuestion test1 = new YesNoQuestion("Is Java Cool?", "Very cool", "Nah. Not my cup of tea", 1);
        test1.printQuestion();
        System.out.println(test1.toXML());
//        Scanner scanner = new Scanner(System.in); // Create a Scanner object to direct stdin
//
//        test1.answerQuestion(scanner);
////        System.out.println(test1.getPointsAwarded());
//        test1.answerQuestion(scanner);
////        System.out.println(test1.getPointsAwarded());
//        test1.answerQuestion(scanner);
////        System.out.println(test1.getPointsAwarded());
//        scanner.close();
    }
}
