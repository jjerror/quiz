package com.example.joshua.quiz;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.joshua.quiz.Fragment.QuizEndFragment;
import com.example.joshua.quiz.Fragment.QuizQuestionFragment;
import com.example.joshua.quiz.Fragment.QuizStartFragment;
import com.example.joshua.quiz.event.QuestionAnswerEvent;
import com.example.joshua.quiz.event.RestartQuizEvent;
import com.example.joshua.quiz.event.StartQuizEvent;
import com.example.joshua.quiz.event.UpdateTimerEvent;
import com.example.joshua.quiz.model.Question;
import com.example.joshua.quiz.model.Quiz;
import com.example.joshua.quiz.model.QuizEntry;
import com.squareup.otto.Subscribe;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    private static final int TIMEOUT_SECONDS = 120;
    private static final int NUMBER_OF_QUESTIONS = 10;

    private Quiz mQuiz;
    private QuizEntry mCurrentQuiz;
    private Timer mTimer;

    @InjectView(R.id.timer) TextView timerView;
    @InjectView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        InputStream inputStream = getResources().openRawResource(R.raw.questions);
        mQuiz = Quiz.loadQuiz(inputStream);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        startLanding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidBus.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AndroidBus.getInstance().unregister(this);
    }

    @Subscribe
    public void startQuiz(StartQuizEvent event) {
        mCurrentQuiz = mQuiz.createNewQuizEntry(NUMBER_OF_QUESTIONS);
        startTimer();
        startQuestion();
    }

    @Subscribe
    public void startQuiz(RestartQuizEvent event) {
        startLanding();
    }

    @Subscribe
    public void answerQuestion(QuestionAnswerEvent event) {
        mCurrentQuiz.answer(event.selectedAnswer);
        if (mCurrentQuiz.nextQuestion() != null) {
            startQuestion();
        } else {
            endQuiz();
        }
    }

    @Subscribe
    public void updateTimer(UpdateTimerEvent event) {
        if (mCurrentQuiz.isExpired(TIMEOUT_SECONDS)) {
            endQuiz();
        }
        long elapsedTime = mCurrentQuiz.getElapsedTime();
        timerView.setText(formatTimerString(elapsedTime));
    }

    private String formatTimerString(long elapsedTime) {
        long secondLeft = TIMEOUT_SECONDS - elapsedTime;
        secondLeft = Math.max(0, secondLeft);
        return String.format("%02d:%02d", secondLeft / Util.SECOND_TO_MINUTE, secondLeft % Util.SECOND_TO_MINUTE);
    }

    private void startLanding() {
        timerView.setText(formatTimerString(0));
        mCurrentQuiz = null;
        loadFragment(QuizStartFragment.newInstance(), QuizStartFragment.TAG);
    }

    private void startQuestion() {
        Question question = mCurrentQuiz.nextQuestion();
        QuizQuestionFragment fragment = QuizQuestionFragment.newInstance(mCurrentQuiz.questionNumber(), question.getTitle(), question.getRandomAnswers());
        loadFragment(fragment, QuizQuestionFragment.TAG);
    }

    private void endQuiz() {
        stopTimer();
        int result = mCurrentQuiz.getResult();
        boolean timeout = mCurrentQuiz.isExpired(TIMEOUT_SECONDS);
        QuizEndFragment fragment = QuizEndFragment.newInstance(result, mCurrentQuiz.getNumberOfQuestion(), timeout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, QuizQuestionFragment.TAG).commitAllowingStateLoss();
    }

    private void startTimer() {
        mCurrentQuiz.startQuiz();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                AndroidBus.getInstance().postOnMain(new UpdateTimerEvent());
            }
        }, 0, Util.MILLISECOND_TO_SECOND);
    }

    private void stopTimer() {
        mCurrentQuiz.endQuiz();
        mTimer.cancel();
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, tag).commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
}
