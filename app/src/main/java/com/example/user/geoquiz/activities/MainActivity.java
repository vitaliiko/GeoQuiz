package com.example.user.geoquiz.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.model.QuizUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addClickListener();
    }

    private void addClickListener() {
        View architecturalQuiz = findViewById(R.id.architecturalQuiz);
        architecturalQuiz.setOnClickListener(this);

        View modernBuildingsQuiz = findViewById(R.id.modernBuildingsQuiz);
        modernBuildingsQuiz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, QuizActivity.class);
        Quiz quiz = null;
        switch (v.getId()) {
            case R.id.architecturalQuiz:{
                quiz = QuizUtil.loadQuiz(this, R.raw.architectural_monuments);
                quiz.setName("Architectural monuments");
                quiz.setDescription("Try to guess where architectural buildings are located");
                break;
            }
            case R.id.modernBuildingsQuiz: {
                quiz = QuizUtil.loadQuiz(this, R.raw.modern_buildings);
                quiz.setName("Architectural monuments");
                quiz.setDescription("Try to guess where modern buildings are located");
                break;
            }
        }
        intent.putExtra("quiz", quiz);
        startActivity(intent);
    }
}
