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
