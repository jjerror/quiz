package com.example.joshua.quiz.model;

import com.example.joshua.quiz.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Immutable Quiz Object
 */
public class Quiz {

    private final Map<String, Question> mQuestions;

    /**
     * Factory method to load the questions from an input stream
     *
     * @param inputStream input stream
     * @return Quiz object if the input is correct, othewise return null
     */
    public static Quiz loadQuiz(InputStream inputStream) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Question> questions = new LinkedHashMap<>();
            Map<String, List<String>> input = mapper.readValue(inputStream, new TypeReference<Map<String, List<String>>>() {
            });
            for (String title : input.keySet()) {
                List<String> images = input.get(title);
                if (images != null) {
                    questions.put(title, new Question(title, images));
                }
            }
            return new Quiz(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Quiz(Map<String, Question> questions) {
        this.mQuestions = questions;
    }

    /**
     * Start a new quiz entry, randomize the questions and create a quiz entry with numberOfQuestion questions
     *
     * @param numberOfQuestion number of questions
     * @return QuizEntry Object
     */
    public QuizEntry createNewQuizEntry(int numberOfQuestion) {
        List<Question> copy = Util.copyAndShuffle(mQuestions.values());
        numberOfQuestion = Math.min(numberOfQuestion, copy.size());
        return new QuizEntry(copy.subList(0, numberOfQuestion));
    }

    /**
     * Get the question object by title
     *
     * @param title title to get
     * @return null if not found
     */
    public Question getQuestion(String title) {
        return mQuestions.get(title);
    }
}
