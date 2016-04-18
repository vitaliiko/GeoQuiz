package com.example.user.geoquiz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Quiz;

public class StartQuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Quiz quiz;
    private int imageId;

    private TextView quizNameText;
    private ImageView image;
    private TextView quizDescriptionText;
    private TextView quizAttemptsText;
    private TextView quizBestResultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        imageId = (Integer) getIntent().getSerializableExtra("titleImage");

        initViews();
        prepareQuizInfo();
    }

    @Override
    public void onClick(View v) {

    }

    private void initViews() {
        quizNameText = (TextView) findViewById(R.id.quizName);
        image = (ImageView) findViewById(R.id.titleImage);
        quizDescriptionText = (TextView) findViewById(R.id.quizDescription);
        quizAttemptsText = (TextView) findViewById(R.id.quizAttempts);
        quizBestResultText = (TextView) findViewById(R.id.quizBestResult);
    }

    private void prepareQuizInfo() {
        quizNameText.setText(quiz.getName());
        image.setImageResource(imageId);
        quizDescriptionText.setText(quiz.getDescription());
        quizAttemptsText.setText(Integer.toString(quiz.getAttempts()));
        quizBestResultText.setText(Integer.toString(quiz.getBestResult()));
    }
}
