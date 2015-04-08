package com.example.joshua.quiz.model;

import com.example.joshua.quiz.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * QuizEntry Object, model each quiz entry
 */
public class QuizEntry {

    private final List<String> mAnswers = new ArrayList<>();
    private final List<Question> mQuestions;

    private long mStartedTime;
    private long mEndedTime;

    /**
     * Constructor
     *
     * @param questions questions for this quiz
     */
    public QuizEntry(List<Question> questions) {
        this.mQuestions = questions;
    }

    /**
     * Answer a question
     *
     * @param answer answer to the current question
     */
    public void answer(String answer) {
        mAnswers.add(answer);
    }

    /**
     * Get the quiz result
     *
     * @return the number of correct answers
     */
    public int getResult() {
        int result = 0;
        for (int i = 0; i < mAnswers.size(); i++) {
            if (mQuestions.get(i).isCorrectAns(mAnswers.get(i))) {
                result++;
            }
        }
        return result;
    }

    /**
     * Get next question
     *
     * @return the next question
     */
    public Question nextQuestion() {
        if (mAnswers.size() < mQuestions.size()) {
            return mQuestions.get(mAnswers.size());
        }
        return null;
    }

    /**
     * @return the question number for the current question
     */
    public int questionNumber() {
        return mAnswers.size() + 1;
    }

    /**
     * @return total number of questions
     */
    public int getNumberOfQuestion() {
        return mQuestions.size();
    }

    /**
     * Start doing the quiz, log the start time
     */
    public void startQuiz() {
        mStartedTime = System.currentTimeMillis();
        mEndedTime = 0;
    }

    /**
     * End doing the quiz, log the end time
     */
    public void endQuiz() {
        mEndedTime = System.currentTimeMillis();
    }

    /**
     * Check if the quiz has expired, if the end time has not logged, use current time to check
     *
     * @param timeout time in seconds
     * @return true if the quiz has expired
     */
    public boolean isExpired(int timeout) {
        return (Math.max(System.currentTimeMillis(), mEndedTime) - mStartedTime) > timeout * Util.MILLISECOND_TO_SECOND;
    }

    /**
     * @return the time since the start of the quiz in seconds
     */
    public long getElapsedTime() {
        long elapsedTime = Math.max(System.currentTimeMillis(), mEndedTime) - mStartedTime;
        return elapsedTime / Util.MILLISECOND_TO_SECOND;
    }

}
