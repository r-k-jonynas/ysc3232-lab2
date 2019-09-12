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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
    A class for loading Exam objects into memory from XML files
 */
public class ExamLoader {
    /*
    A function that takes a filename of an XML file, unpacks its contents and creates an Exam object from them.
    Adapted from readXMLFile function in Planner project
    (Written by @author: professor Bruno Bodin) and extended for my case
     */
    public static Exam loadExam(String filename) throws IOException, SAXException {
        Exam resExam;
        File inputXMLFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputXMLFile);
            doc.getDocumentElement().normalize();

            Element examE = doc.getDocumentElement();
//            String examProducer =
            resExam = new Exam();

            List<Question> newQuestionList = new ArrayList<>();

            NodeList questionElements = examE.getElementsByTagName("question");
            for (int i = 0; i < questionElements.getLength(); i++) {
                Node questionN = questionElements.item(i);

                if (questionN.getNodeType() == Node.ELEMENT_NODE) {
                    Element questionE = (Element) questionN;
                    String questionType = questionE.getAttribute("type");

                    switch (questionType) {
                        // Unpack XML schema for YesNoQuestion and create a YesNoQuestion object
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
                            // Add the new question to the question list
                            YesNoQuestion tempQ = new YesNoQuestion(questionTextYNQ, yes, no, correct);
                            newQuestionList.add(tempQ);
                            break;
                        }

                        // Unpack XML schema for MultipleChoiceQuestion and create a MultipleChoiceQuestion object
                        case "MultipleChoice": {
                            String questionTextMCQ = questionE.getAttribute("questionText");
                            NodeList optionElementsMCQ = questionE.getElementsByTagName("option");
                            Set<String> correctOptions = new HashSet<>();
                            Set<String> allOptions = new HashSet<>();
                            // Unpack all answer options
                            for (int j = 0; j < optionElementsMCQ.getLength(); j++) {
                                Node optionN = optionElementsMCQ.item(j);
                                if (optionN.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optionE = (Element) optionN;
                                    if (optionE.getAttribute("correctness").equals("Correct")) {
                                        correctOptions.add(optionE.getAttribute("name"));
                                    } else if ((!optionE.getAttribute("correctness").equals("Incorrect"))
                                            && (!optionE.getAttribute("correctness").equals("Correct"))) {
                                        throw new InputMismatchException(); // Not exactly sure if this is an appropriate error
                                    }
                                    allOptions.add(optionE.getAttribute("name"));
                                }
                            }
                            // Add the new question to the question list
                            MultipleChoiceQuestion tempQ = new MultipleChoiceQuestion(questionTextMCQ, allOptions, correctOptions);
                            newQuestionList.add(tempQ);
                            break;
                        }
                        // Unpack XML schema for OpenQuestion and create a OpenQuestion object
                        case "Open": {
                            String questionTextOQ = questionE.getAttribute("questionText");
                            OpenQuestion tempQ = new OpenQuestion(questionTextOQ);
                            // Add the new question to the question list
                            newQuestionList.add(tempQ);
                            break;
                        }
                    }
                }
            }

            // Add all the questions in the question list to the question list of the Exam object
            resExam.addQuestions(newQuestionList);
            return resExam;

        } catch (ParserConfigurationException e) {
            Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null,
                    e);
        }
        return null;
    }
}
