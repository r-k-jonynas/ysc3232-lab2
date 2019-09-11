import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Exam implements XMLizable{
    List<Question> questionList;
    String producer;
    Date dateCreated;
    Date dateTaken;
    ExamStatus status;


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

    public void addQuestions (List<Question> newQuestions) {
        for (Question q: newQuestions) {
            this.questionList.add(q);
        }
    }

    public void releaseExam() {
        this.status = ExamStatus.RELEASED;
    }

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

    @Override
    public String toXML() {
        String qstring = "";
        for (Question q : this.questionList) {
            qstring += "\n    ";
            qstring += q.toXML();
        }
        return "<exam>" + qstring + "</exam>";
    }

    /*
    Applied from saveXMLFile function in Planner project
    written by @author: professor Bruno Bodin
     */
    void saveXMLFile(String filename) {
        String str = this.toXML();
        try {
            Files.write(Paths.get(filename), str.getBytes());
        } catch (IOException e) {
            Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    public static Exam loadExam(String filename) throws IOException, SAXException {
        Exam resExam;
        File inputXMLFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputXMLFile);
            doc.getDocumentElement().normalize();
//            System.out.println("Root element :" +
//                    doc.getDocumentElement().getNodeName());
            Element examE = doc.getDocumentElement();
//            String examProducer =
            resExam = new Exam();

            List<Question> newQuestionList = new ArrayList<>();

            NodeList questionElements = examE.getElementsByTagName("question");
            for (int i = 0; i < questionElements.getLength(); i++) {
                Node questionN = questionElements.item(i);
//                System.out.println("\nCurrent Element :" + questionN.getNodeName());
                if (questionN.getNodeType() == Node.ELEMENT_NODE) {
                    Element questionE = (Element) questionN;
                    String questionType = questionE.getAttribute("type");

                    switch (questionType) {
                        case "YesNoQuestion": {
                            String questionTextYNQ = questionE.getAttribute("questionText");
                            NodeList optionElementsYNQ = questionE.getElementsByTagName("option");
                            String yes = "Y"; String no = "N";
                            int correct = 1;
                            for (int j = 0; j < optionElementsYNQ.getLength(); j++) {
                                Node optionN = optionElementsYNQ.item(j);
                                if (optionN.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optionE = (Element) optionN;
                                    String optionType = optionE.getAttribute("optionType");
                                    if (optionType.equals("No")) {
                                        no = optionE.getAttribute("name");
                                    } else if (optionType.equals("Yes")) {
                                        yes = optionE.getAttribute("name");
                                        String correctness = optionE.getAttribute("correctness");
                                        correct = (correctness.equals("Correct")) ? 1 : 0;
                                    } else {
                                        throw new InputMismatchException(); // Not exactly sure if this is an appropriate error
                                    }
                                }
                            }

                            YesNoQuestion tempQ = new YesNoQuestion(questionTextYNQ, yes, no, correct);
                            newQuestionList.add(tempQ);
                            break;
                        }

                        case "MultipleChoice": {
                            String questionTextMCQ = questionE.getAttribute("questionText");
                            NodeList optionElementsMCQ = questionE.getElementsByTagName("option");
                            Set<String> correctOptions = new HashSet<>();
                            Set<String> allOptions = new HashSet<>();
                            for (int j = 0; j < optionElementsMCQ.getLength(); j++) {
                                Node optionN = optionElementsMCQ.item(j);
                                if (optionN.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optionE = (Element) optionN;
//                                    System.out.println(optionE.getAttribute("name"));
//                                    System.out.println(optionE.getAttribute("correctness"));
                                    if (optionE.getAttribute("correctness").equals("Correct")) {
                                        correctOptions.add(optionE.getAttribute("name"));
                                    } else if ((!optionE.getAttribute("correctness").equals("Incorrect"))
                                            && (!optionE.getAttribute("correctness").equals("Correct"))) {
                                        throw new InputMismatchException(); // Not exactly sure if this is an appropriate error
                                    }
                                    allOptions.add(optionE.getAttribute("name"));
                                }
                            }
                            MultipleChoiceQuestion tempQ = new MultipleChoiceQuestion(questionTextMCQ, allOptions, correctOptions);
                            newQuestionList.add(tempQ);
                            break;
                        }

                        case "Open": {
                            String questionTextOQ = questionE.getAttribute("questionText");
                            OpenQuestion tempQ = new OpenQuestion(questionTextOQ);
                            newQuestionList.add(tempQ);
                            break;
                        }
                    }
                }
            }
            resExam.addQuestions(newQuestionList);
            return resExam;
        } catch (ParserConfigurationException e) {
            Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null,
                    e);
        }
        return null;
    }

    public static void demo() {
        Exam test = new Exam();
//        System.out.println(test.dateCreated);
//        System.out.println(test.dateTaken);
//        System.out.println(test.status);
        test.addDefaultQuestionSet();

        System.out.println(test.toXML());
        test.saveXMLFile("exam_test1.xml");
        try {
            Exam test_loaded = Exam.loadExam("exam_test1.xml");
            System.out.println("----------------");
            System.out.println(test_loaded.toXML());
            test_loaded.saveXMLFile("test_loaded.xml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
////        System.out.println(test.dateCreated);
//        System.out.println(test.dateTaken);
//        System.out.println(test.status);
//        System.out.println("------------");
//        test.releaseExam();
//        try {
//            test.takeExam();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        System.out.println("Executed!");
    }

    public static void main(String[] args) {
        demo();
    }

}
