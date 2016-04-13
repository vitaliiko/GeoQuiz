package com.example.user.geoquiz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private int currentQuestionNum = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        prepareQuizInfo();
        addListenerOnStartButton();
        addListenerOnNavigationButtons();
    }

    private void addListenerOnStartButton() {
        Button startButton = (Button) findViewById(R.id.startButton);
        assert startButton != null;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup startLayout = (ViewGroup) findViewById(R.id.startLayout);
                assert startLayout != null;
                startLayout.setVisibility(View.GONE);

                ViewGroup questionLayout = (ViewGroup) findViewById(R.id.questionLayout);
                assert questionLayout != null;
                questionLayout.setVisibility(View.VISIBLE);

                prepareQuestion();
            }
        });
    }

    private void addListenerOnNavigationButtons() {
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        assert nextButton != null;

        final Button prevButton = (Button) findViewById(R.id.prevButton);
        assert prevButton != null;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextButton.getText().toString().equals("Next")) {
                    checkResult();
                    currentQuestionNum++;
                    prevButton.setEnabled(currentQuestionNum > 0);
                    prepareQuestion();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionNum--;
                nextButton.setText(currentQuestionNum == quiz.getQuestions().size() - 1 ? "Done" : "Next");
                prepareQuestion();
            }
        });
    }

    private void checkResult() {
        RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        assert answersRadioGroup != null;

        int answerId = answersRadioGroup.getCheckedRadioButtonId();
        if (answerId > -1) {
            userAnswers.put(currentQuestionNum, answerId);
        }
    }

    private void prepareQuizInfo() {
        TextView quizName = (TextView) findViewById(R.id.quizName);
        assert quizName != null;
        quizName.setText(quiz.getName());

        TextView quizDescription = (TextView) findViewById(R.id.quizDescription);
        assert quizDescription != null;
        quizDescription.setText(quiz.getDescription());

        TextView quizAttempts = (TextView) findViewById(R.id.quizAttempts);
        assert quizAttempts != null;
//        quizAttempts.setText(quiz.getAttempts());

        TextView quizBestResult = (TextView) findViewById(R.id.quizBestResult);
        assert quizBestResult != null;
//        quizBestResult.setText(quiz.getBestResult());
    }

    private void prepareQuestion() {
        Question question = quiz.getQuestions().get(currentQuestionNum);

//        TextView currentQuestionNum = (TextView) findViewById(R.id.currentQuestionNum);
//        assert currentQuestionNum != null;
//        currentQuestionNum.setText(this.currentQuestionNum + 1);
//
//        TextView questionsCount = (TextView) findViewById(R.id.questionsCount);
//        assert questionsCount != null;
//        questionsCount.setText(quiz.getQuestions().size());

        TextView questionText = (TextView) findViewById(R.id.questionText);
        assert questionText != null;
        questionText.setText(question.getQuestionText());

        RadioGroup answersGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        assert answersGroup != null;
        answersGroup.removeAllViews();
        for (String answer : question.getAnswers()) {
            RadioButton button = new RadioButton(this);
            button.setText(answer);
            answersGroup.addView(button);
        }
    }
}
