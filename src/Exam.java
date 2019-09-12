import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Exam class.
    Stores and manipulates exam objects.
    Allows the functionality of adding questions, saving exam as XML, and running exam session in the terminal.
 */
public class Exam implements XMLizable{
    List<Question> questionList;
    String producer;
    Date dateCreated;
    Date dateTaken;
    ExamStatus status;

    /*
    Default exam constructor
     */
    public Exam() {
        this.questionList = new ArrayList<Question>();
        this.producer = "RYTIS";
        this.dateCreated = new Date(); // Gets current date
        this.status = ExamStatus.IN_THE_MAKING;
    }

    /*
    For creating a copy of a custom exam (e.g. from XML)
     */
    public Exam(String whoMadeExam, Date whenExamWasMade) {
        this.questionList = new ArrayList<Question>();
        this.producer = whoMadeExam;
        this.dateCreated = whenExamWasMade;
        this.status = ExamStatus.IN_THE_MAKING;
    }

    /*
    A getter for questionList
     */
    public List<Question> getQuestionList() {
        return this.questionList;
    }

    /*
    Create a demo version of a question list
     */
    private void addDefaultQuestionSet() {
        YesNoQuestion q1 = new YesNoQuestion("Is Java compiled to JVM bytecode?", "Yes", "No / Not sure", 1);
        this.questionList.add(q1);

        Set<String> all_langs = new HashSet<>();
        all_langs.add("Python");
        all_langs.add("Perl");
        all_langs.add("Rust");
        all_langs.add("JavaScript");
        all_langs.add("C++");
        Set<String> interpreted_langs = new HashSet<>();
        interpreted_langs.add("Python");
        interpreted_langs.add("Perl");
        interpreted_langs.add("JavaScript");

        MultipleChoiceQuestion q2 = new MultipleChoiceQuestion("Which of these languages are interpreted?", all_langs, interpreted_langs);
        this.questionList.add(q2);

        OpenQuestion q3 = new OpenQuestion("What does operating system do when you type python script.py in the terminal?");
        this.questionList.add(q3);
        this.status = ExamStatus.CREATED;
    }

    /*
    Method for adding new Questions to the existing question list of the Exam Object
     */
    public void addQuestions (List<Question> newQuestions) {
        for (Question q: newQuestions) {
            this.questionList.add(q);
        }
    }

    /*
    Changes the status of the Exam to RELEASED (ready for answering)
     */
    public void releaseExam() {
        this.status = ExamStatus.RELEASED;
    }

    /*
    Iterates over question and provides an interface for answering each of them and saving the answers.
     */
    public void takeExam() throws IllegalAccessException {
        if (this.status != ExamStatus.RELEASED) {
            throw new IllegalAccessException("The exam has either been released yet or has already been finished.");
        } else {
            Scanner scanner = new Scanner(System.in); // Create a Scanner object to direct stdin
            for (Question q:
                 this.questionList) {
                q.printQuestion();
                q.answerQuestion(scanner);
            }
            scanner.close();
        }
    }

    /*
    Generates a string from the Exam object.
     */
    @Override
    public String toXML() {
        String qstring = "";
        for (Question q : this.questionList) {
            qstring += "\n    ";
            qstring += q.toXML();
        }
        return "<exam>" + qstring + "</exam>";
    }

    /* A function that takes XML and saves it into a text file (.xml).
    Taken from saveXMLFile function in Planner project
    (written by @author: professor Bruno Bodin)
     */
    void saveXMLFile(String filename) {
        String str = this.toXML();
        try {
            Files.write(Paths.get(filename), str.getBytes());
        } catch (IOException e) {
            Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /*
    Demo for showing functionality for:
        1) creating XML file for Exam object;
        2) creating Exam object from XML file;
        3) demoing Exam session - answering questions in the terminal.
     */
    public static void demo() {
        Exam test = new Exam();
        test.addDefaultQuestionSet();

        System.out.println(test.toXML());
        test.saveXMLFile("exam_test1.xml");
        try {
            Exam test_loaded = ExamLoader.loadExam("exam_test1.xml");
            System.out.println("----------------");
            System.out.println(test_loaded.toXML());

            // Release the exam
            test_loaded.releaseExam();
            // Start exam session
            test_loaded.takeExam();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Exam was not released");
            e.printStackTrace();
        }


        System.out.println("Executed!");
    }

    public static void main(String[] args) {
        demo();
    }

}
