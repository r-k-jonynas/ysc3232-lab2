# Lab2 : Exam project

## Project Structure
-   Lab2QCM - main file. Run the demo from here.
-   Exam - a class for exam object and all its functionality
-   ExamLoader - a class for loading Exam from XML
-   Question - abstract class which is subclassed by all types of questions
    -   MultipleChoiceQuestion - a class for Multiple Choice question
    -   OpenQuestion - a class for Open Ended question
    -   YesNoQuestion - a class for dichotomous (Yes / No) question with customizable Yes and No names.
-   XMLizable - interface for all object classes which can be converted to XML
-   ExamStatus - enum class for the states of the exam

## Reasoning for this project structure
1.  Creating a) an abstract `Question` class which implements `XMLizable` interface and extending it in each `TypeOfQuestion` subclass vs. b) Creating separate `Question`, `XMLizable` interfaces and making each `TypeQuestion` implement both:

    Creating abstract `Question` class for all questions, allows me to make all `TypeOfQuestion` subclasses inherit from XMLizable. This design decision prevents issues of dynamic and static type clashes, which would arise if I made each `TypeOfQuestion` subclass directly inherit from `XMLizable` and would call `toXML()` method when iterating over the list of questions (`List<Question>`).
    
    e.g. the following would lead to an error in b) implementation (2 separate interfaces) but works perfectly fine with a) implementation (`Question` abstract class which implements `XMLizable` interface)
    ``` java
    for (Question q : this.questionList) {
                qstring += "\n    ";
                qstring += q.toXML();
            }
    ```
2.  Creating `ExamLoader` class for `loadXML()`:

    `ExamLoader` class uses a very long and complex method. I thought the implementation and the function of this method would be easier to understand if it was in a separate file. 

3. Creating `ExamStatus` enum class:

    I decided to use a separate enum class for ExamStatus for simplicity and clearer code.