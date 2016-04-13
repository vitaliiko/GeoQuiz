package com.example.user.goeguiz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.goeguiz.R;
import com.example.user.goeguiz.model.Question;
import com.example.user.goeguiz.model.Quiz;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        prepareQuestion(quiz.getQuestions().get(0));
    }

    private void prepareQuestion(Question question) {
        TextView questionText = (TextView) findViewById(R.id.questionText);
        assert questionText != null;
        questionText.setText(question.getQuestionText());

        RadioGroup answersGroup = (RadioGroup) findViewById(R.id.answersGroup);
        assert answersGroup != null;
        for (String answer : question.getAnswers()) {
            RadioButton button = new RadioButton(this);
            button.setText(answer);
            answersGroup.addView(button);
        }
    }
}
