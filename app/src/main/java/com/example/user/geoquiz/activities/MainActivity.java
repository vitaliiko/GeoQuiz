package com.example.user.geoquiz.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.QuizProvider;

public class MainActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addClickListener();
    }

    private void addClickListener() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.quiz1);
        assert layout != null;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("quiz", QuizProvider.prepareQuiz());
                startActivity(intent);
            }
        });
    }
}
