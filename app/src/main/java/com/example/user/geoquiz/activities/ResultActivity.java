package com.example.user.geoquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.utils.QuizUtil;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Quiz quiz;

    private TextView messageText;
    private TextView resultText;
    private TextView bestResultText;
    private TextView spentTimeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName());

        initViews();
        prepareResultInfo();
        setOnClickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_try_again: {
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra(Quiz.class.getSimpleName(), quiz);
                startActivity(intent);
                break;
            }
            case R.id.btn_main_menu: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void setOnClickListener() {
        findViewById(R.id.btn_try_again).setOnClickListener(this);
        findViewById(R.id.btn_main_menu).setOnClickListener(this);
    }

    private void initViews() {
        messageText = (TextView) findViewById(R.id.text_message);
        resultText = (TextView) findViewById(R.id.text_result);
        bestResultText = (TextView) findViewById(R.id.text_best_result);
        spentTimeText = (TextView) findViewById(R.id.text_spent_time);
    }

    private void prepareResultInfo() {
        int stringId = QuizUtil.generateMessage(quiz.getLastResult());
        messageText.setText(getString(stringId));
        bestResultText.setText(Integer.toString(quiz.getBestResult()));
        spentTimeText.setText(quiz.getSpentTime());
        resultText.setText(Integer.toString(quiz.getLastResult()) + " "
                + getString(R.string.of) + " " + QuizUtil.QUESTIONS_COUNT);
    }
}
