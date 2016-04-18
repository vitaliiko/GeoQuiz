package com.example.user.geoquiz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.utils.QuizUtil;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView messageText;
    private TextView resultText;
    private TextView bestResultText;
    private TextView spentTimeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

//    private void initViews() {
//        messageText = (TextView) findViewById(R.id.message);
//        resultText = (TextView) findViewById(R.id.result);
//        bestResultText = (TextView) findViewById(R.id.bestResult);
//        spentTimeText = (TextView) findViewById(R.id.spentTime);
//    }
//
//    private void prepareResultInfo() {
//        messageText.setText(QuizUtil.generateMessage(result));
//
//        resultText.setText(Integer.toString(this.result) + " of " + QuizUtil.QUESTIONS_COUNT);
//
//        bestResultText.setText(Integer.toString(quiz.getBestResult()));
//
//        spentTimeText.setText(QuizUtil.countSpentTime(startTime));
//    }
}
