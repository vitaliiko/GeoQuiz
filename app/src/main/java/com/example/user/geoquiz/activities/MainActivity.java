package com.example.user.geoquiz.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.utils.QuizUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addClickListener();
    }

    private void addClickListener() {
        findViewById(R.id.layout_architectural_quiz).setOnClickListener(this);
        findViewById(R.id.layout_modernBuildings_quiz).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, StartQuizActivity.class);
        Quiz quiz = null;
        switch (v.getId()) {
            case R.id.layout_architectural_quiz: {
                quiz = QuizUtil.loadQuiz(this, R.raw.architectural_monuments);
                quiz.setName(getString(R.string.architectural_monuments));
                quiz.setDescription("Try to guess where architectural buildings are located");
                quiz.setMainImage(R.drawable.image1);
                break;
            }
            case R.id.layout_modernBuildings_quiz: {
                quiz = QuizUtil.loadQuiz(this, R.raw.modern_buildings);
                quiz.setName(getString(R.string.modern_buildings));
                quiz.setDescription("Try to guess where modern buildings are located");
                quiz.setMainImage(R.drawable.image12);
                break;
            }
        }
        intent.putExtra(Quiz.class.getSimpleName(), quiz);
        startActivity(intent);
    }
}
