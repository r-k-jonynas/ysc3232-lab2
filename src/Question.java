import java.util.Scanner;

public abstract class Question implements XMLizable {

    abstract void printQuestion();
    abstract void answerQuestion(Scanner sc);
    abstract int getPointsAwarded();
//    abstract String toXML ();
}
