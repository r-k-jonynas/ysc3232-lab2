import java.util.Scanner;

/*
    Abstract class for all Questions
 */
public abstract class Question implements XMLizable {

    abstract void printQuestion();
    abstract void answerQuestion(Scanner sc);
    abstract int getPointsAwarded();
}
