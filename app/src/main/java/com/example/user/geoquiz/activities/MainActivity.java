package com.example.user.geoquiz.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.QuizUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        QuizUtil.prepareQuiz(this);
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
        startActivity(intent);
    }
}
