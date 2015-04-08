package com.example.joshua.quiz.model;

import com.example.joshua.quiz.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Immutable Quiz Object
 */
public class Quiz {

    private final List<Question> mQuestions;

    /**
     * Factory method to load the questions from an input stream
     *
     * @param inputStream input stream
     * @return Quiz object if the input is correct, othewise return null
     */
    public static Quiz loadQuiz(InputStream inputStream) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Question> questions = new ArrayList<>();
            Map<String, List<String>> input = mapper.readValue(inputStream, new TypeReference<Map<String, List<String>>>() {
            });
            for (String title : input.keySet()) {
                List<String> images = input.get(title);
                if (images != null) {
                    questions.add(new Question(title, images));
                }
            }
            return new Quiz(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Quiz(List<Question> questions) {
        this.mQuestions = questions;
    }

    /**
     * Start a new quiz entry, randomize the questions and create a quiz entry with numberOfQuestion questions
     *
     * @param numberOfQuestion number of questions
     * @return QuizEntry Object
     */
    public QuizEntry createNewQuizEntry(int numberOfQuestion) {
        List<Question> copy = Util.copyAndShuffle(mQuestions);
        numberOfQuestion = Math.min(numberOfQuestion, copy.size());
        return new QuizEntry(copy.subList(0, numberOfQuestion));
    }

}
