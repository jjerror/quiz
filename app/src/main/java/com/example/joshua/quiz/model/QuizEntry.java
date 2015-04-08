package com.example.joshua.quiz.model;

import com.example.joshua.quiz.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * QuizEntry Object, model each quiz entry
 */
public class QuizEntry {

    private final List<String> mAnswers;
    private final List<Question> mQuestions;

    private long mStartedTime;
    private long mEndedTime;

    /**
     * Constructor
     *
     * @param questions questions for this quiz
     */
    public QuizEntry(List<Question> questions) {
        mQuestions = questions;
        mAnswers = new ArrayList<>(questions.size());
    }

    /**
     * Constructor from a saved json string
     *
     * @param quiz the quiz
     * @param json json to load
     * @throws IOException
     */
    public QuizEntry(Quiz quiz, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Map input = mapper.readValue(inputStream, Map.class);

        //noinspection unchecked
        List<String> questions = (List<String>) input.get("questions");
        mQuestions = new ArrayList<>(questions.size());
        for (String title : questions) {
            mQuestions.add(quiz.getQuestion(title));
        }
        //noinspection unchecked
        mAnswers = (List<String>) input.get("answers");
        mStartedTime = (Long) input.get("start");
        if (input.get("end") instanceof Long) {
            mEndedTime = (Long) input.get("end");
        }
        if (input.get("end") instanceof Integer) {
            mEndedTime = (Integer) input.get("end");
        }
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
        if (mStartedTime == 0) {
            mStartedTime = System.currentTimeMillis();
            mEndedTime = 0L;
        }
    }

    /**
     * End doing the quiz, log the end time
     */
    public void endQuiz() {
        if (mEndedTime == 0L) {
            mEndedTime = System.currentTimeMillis();
        }
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
        long endTime = mEndedTime == 0 ? System.currentTimeMillis() : mEndedTime;
        long elapsedTime = endTime - mStartedTime;
        return elapsedTime / Util.MILLISECOND_TO_SECOND;
    }

    /**
     * Serial to json string
     *
     * @return the json string
     */
    public String toJson() {
        Map<String, Object> serialize = new LinkedHashMap<>();
        List<String> questions = new ArrayList<>(mQuestions.size());
        for (Question question : mQuestions) {
            questions.add(question.getTitle());
        }
        serialize.put("questions", questions);
        serialize.put("answers", mAnswers);
        serialize.put("start", mStartedTime);
        serialize.put("end", mEndedTime);
        try {
            ObjectMapper mapper = new ObjectMapper();
            OutputStream outputStream = new ByteArrayOutputStream();
            mapper.writeValue(outputStream, serialize);
            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
