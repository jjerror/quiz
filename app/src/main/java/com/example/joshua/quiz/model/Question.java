package com.example.joshua.quiz.model;

import com.example.joshua.quiz.Util;

import java.util.List;

/**
 * immutable Question Object
 */
public class Question {

    private final String mTitle;
    private final List<String> mAnswers;

    /**
     * Constructor
     *
     * @param title   title
     * @param answers answers
     */
    public Question(String title, List<String> answers) {
        this.mTitle = title;
        this.mAnswers = answers;
    }

    /**
     * @param answer answer
     * @return true if the answer is correct
     */
    public boolean isCorrectAns(String answer) {
        // the first answer is always the correct answer
        return mAnswers.get(0).equals(answer);
    }

    /**
     * @return The multiple choice in random order
     */
    public List<String> getRandomAnswers() {
        return Util.copyAndShuffle(mAnswers);
    }

    /**
     * @return get the title of the question
     */
    public String getTitle() {
        return mTitle;
    }
}
