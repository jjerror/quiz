package com.example.joshua.quiz.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joshua.quiz.AndroidBus;
import com.example.joshua.quiz.R;
import com.example.joshua.quiz.event.StartQuizEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment to start the quiz
 */
public class QuizStartFragment extends Fragment {

    public static final String TAG = "START";

    @InjectView(R.id.text) TextView textView;
    @InjectView(R.id.button) Button startButton;

    /**
     * Factory method to create a new instance of * this fragment to show the introduction of the quiz
     *
     * @return A new instance of fragment QuizStartFragment.
     */
    public static QuizStartFragment newInstance() {
        return new QuizStartFragment();
    }

    public QuizStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        textView.setText(R.string.quiz_description);
        startButton.setText(R.string.Start);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_button, container, false);
    }

    @OnClick(R.id.button)
    void start() {
        AndroidBus.getInstance().post(new StartQuizEvent());
    }
}
