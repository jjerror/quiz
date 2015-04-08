package com.example.joshua.quiz.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.joshua.quiz.Adapter.AnswerAdapter;
import com.example.joshua.quiz.AndroidBus;
import com.example.joshua.quiz.R;
import com.example.joshua.quiz.event.QuestionAnswerEvent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment to display the question
 */
public class QuizQuestionFragment extends Fragment {
    private static final String ARG_CURRENT_NO = "CURRENT_NO";
    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_IMAGES = "IMAGES";
    public static final String TAG = "QUESTION";

    private int mQuestionNumber;
    private String mTitle;
    private String mCurrentAnswer;
    private String[] mAnswers;
    AnswerAdapter mAnswerAdapter;

    @InjectView(R.id.question_number) TextView questionNumberView;
    @InjectView(R.id.title) TextView titleTextView;
    @InjectView(R.id.answers) GridView answerView;
    @InjectView(R.id.submit) Button submitButton;

    /**
     * Factory method to create a new instance of * this fragment to display the question
     *
     * @param questionNumber the question number
     * @param title          the description of the question
     * @param answers        the image urls of the answers
     * @return A new instance of fragment QuizQuestionFragment.
     */
    public static QuizQuestionFragment newInstance(int questionNumber, String title, List<String> answers) {
        String[] images = new String[answers.size()];
        answers.toArray(images);

        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_NO, questionNumber);
        args.putString(ARG_TITLE, title);
        args.putStringArray(ARG_IMAGES, images);
        fragment.setArguments(args);
        return fragment;
    }

    public QuizQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionNumber = getArguments().getInt(ARG_CURRENT_NO);
            mTitle = getArguments().getString(ARG_TITLE);
            mAnswers = getArguments().getStringArray(ARG_IMAGES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        // setup ui
        questionNumberView.setText(getString(R.string.question, mQuestionNumber));
        titleTextView.setText(mTitle);
        submitButton.setEnabled(false);

        // setup the grid view
        mAnswerAdapter = new AnswerAdapter(getActivity(), mAnswers);

        // TODO find a better layout for the 4 items. The layout doesn't good atm.
        answerView.setAdapter(mAnswerAdapter);
        answerView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        answerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                submitButton.setEnabled(true);
                mCurrentAnswer = mAnswers[position];
                view.setSelected(true);
            }
        });
    }

    @OnClick(R.id.submit)
    void submit() {
        AndroidBus.getInstance().post(new QuestionAnswerEvent(mQuestionNumber, mCurrentAnswer));
    }
}
