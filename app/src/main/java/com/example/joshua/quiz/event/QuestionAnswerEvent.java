package com.example.joshua.quiz.event;

/**
 * Event for a question being answered
 */
public class QuestionAnswerEvent {
    public int questionNumber;
    public String selectedAnswer;

    public QuestionAnswerEvent(int questionNumber, String selectedAnswer) {
        this.questionNumber = questionNumber;
        this.selectedAnswer = selectedAnswer;
    }
}
