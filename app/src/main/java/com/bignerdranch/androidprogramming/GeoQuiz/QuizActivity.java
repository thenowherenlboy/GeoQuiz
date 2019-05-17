package com.bignerdranch.androidprogramming.GeoQuiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPrevButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mScoreTextView;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "Answered";
    private int right_answers = 0;
    private int wrong_answers = 0;
    private double percentage_grade;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private boolean[] mQuestionAnswered = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called, he wants his bytes back.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called.");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_ANSWERED,mQuestionAnswered);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionAnswered = savedInstanceState.getBooleanArray(KEY_ANSWERED);
            if (mQuestionAnswered == null) {
                mQuestionAnswered = new boolean[mQuestionBank.length];
            }
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextQuestion();
            }
        });

        mScoreTextView = (TextView) findViewById(R.id.score_view);

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prevQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionAnswered[mCurrentIndex] = true;
                buttConfig();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionAnswered[mCurrentIndex] = true;
                buttConfig();
                checkAnswer(false);
            }
        });

        updateQuestion();
    }
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        buttConfig();
    }

    private void buttConfig() {
        boolean tf = mQuestionAnswered[mCurrentIndex];
        mFalseButton.setEnabled(!tf);
        mTrueButton.setEnabled(!tf);
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex +1) % mQuestionBank.length;
        updateQuestion();
    }

    private void prevQuestion() {
        mCurrentIndex -= 1;
        if (mCurrentIndex < 0) mCurrentIndex = (mQuestionBank.length - 1);
        updateQuestion();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            right_answers += 1;
        } else {
            messageResId = R.string.incorrect_toast;
            wrong_answers += 1;
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
        if ((right_answers + wrong_answers) == mQuestionBank.length) {
            checkFinal();
        }

    }
    private void checkFinal() {
        percentage_grade = ((float) right_answers / (float) mQuestionBank.length) * 100.0;
        String grade = "Percentage answered correctly: " + String.format("%.2f", percentage_grade);
        char[] out = grade.toCharArray();
        mScoreTextView.setText(out, 0, (out.length - 1));

    }
}