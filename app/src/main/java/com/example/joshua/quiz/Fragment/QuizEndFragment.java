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
import com.example.joshua.quiz.event.RestartQuizEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment to display the end of the quiz
 */
public class QuizEndFragment extends Fragment {
    private static final String ARG_RESULT = "RESULT";
    private static final String ARG_TOTAL = "TOTAL";
    private static final String ARG_TIMEOUT = "TIMEOUT";

    private int mResult;
    private int mTotal;
    private boolean mTimeout;

    @InjectView(R.id.text) TextView resultView;
    @InjectView(R.id.button) Button resetButton;

    /**
     * Factory method to create a new instance of this fragment to display the quiz end result
     *
     * @param result  score the user got
     * @param total   total number of question
     * @param timeout is the quiz timed out
     * @return the fragment class
     */
    public static QuizEndFragment newInstance(int result, int total, boolean timeout) {
        QuizEndFragment fragment = new QuizEndFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RESULT, result);
        args.putInt(ARG_TOTAL, total);
        args.putBoolean(ARG_TIMEOUT, timeout);
        fragment.setArguments(args);
        return fragment;
    }

    public QuizEndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        if (bundle != null) {
            this.mResult = bundle.getInt(ARG_RESULT);
            this.mTotal = bundle.getInt(ARG_TOTAL);
            this.mTimeout = bundle.getBoolean(ARG_TIMEOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_button, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        if (mTimeout) {
            resultView.setText(getString(R.string.timeout_result_text, mResult, mTotal));
        } else {
            resultView.setText(getString(R.string.result_text, mResult, mTotal));
        }
        resetButton.setText(R.string.restart);
    }

    @OnClick(R.id.button)
    void start() {
        AndroidBus.getInstance().post(new RestartQuizEvent());
    }
}
