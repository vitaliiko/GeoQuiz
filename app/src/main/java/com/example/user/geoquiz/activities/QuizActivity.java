package com.example.user.geoquiz.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.utils.QuizUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizActivity
        extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Quiz quiz;
    private long startTime;
    private int currentQuestionNum;
    private int maxQuestionNum;
    private List<Question> questions;
    private Set<Integer> showingAnswers = new HashSet<>();
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    private Button nextButton;
    private Button prevButton;
    private Button showAnswerButton;
    private Button completeButton;
    private TextView userAnswerText;
    private TextView rightAnswerText;
    private TextView questionText;
    private TextView currentQuestionNumText;
    private TextView questionsCountText;
    private ImageView image;
    private RadioGroup answersRadioGroup;
    private LinearLayout userAnswerLayout;
    private LinearLayout rightAnswerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName());

        initViews();
        setListeners();
        startQuiz();
    }

    private void initViews() {
        nextButton = (Button) findViewById(R.id.nextButton);
        prevButton = (Button) findViewById(R.id.prevButton);
        showAnswerButton = (Button) findViewById(R.id.showAnswerButton);
        completeButton = (Button) findViewById(R.id.completeButton);

        userAnswerText = (TextView) findViewById(R.id.userAnswerText);
        rightAnswerText = (TextView) findViewById(R.id.rightAnswerText);
        questionText = (TextView) findViewById(R.id.questionText);
        currentQuestionNumText = (TextView) findViewById(R.id.currentQuestionNum);
        questionsCountText = (TextView) findViewById(R.id.questionsCount);

        answersRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        image = (ImageView) findViewById(R.id.quizImage);

        userAnswerLayout = (LinearLayout) findViewById(R.id.userAnswerLayout);
        rightAnswerLayout = (LinearLayout) findViewById(R.id.rightAnswerLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tryAgainButton: {
                startQuiz();
                break;
            }
            case R.id.nextButton: {
                nextQuestion();
                break;
            }
            case R.id.prevButton: {
                prevQuestion();
                break;
            }
            case R.id.showAnswerButton: {
                showRightAnswer();
                break;
            }
            case R.id.mainMenuButton: {
                goToMainMenu();
                break;
            }
            case R.id.completeButton: {
                goToResultActivity();
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setShowAnswerButtonEnabled();
    }

    private void setShowAnswerButtonEnabled() {
        int answerId = answersRadioGroup.getCheckedRadioButtonId();
        if (answerId > -1) {
            showAnswerButton.setEnabled(answerId > -1);
        }
    }

    private void setListeners() {
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        showAnswerButton.setOnClickListener(this);
        answersRadioGroup.setOnCheckedChangeListener(this);
        completeButton.setOnClickListener(this);
    }

    private void nextQuestion() {
        saveUserAnswer();
        answersRadioGroup.clearCheck();
        if (currentQuestionNum == questions.size() - 1) {
            currentQuestionNum = 0;
        } else {
            currentQuestionNum++;
            if (currentQuestionNum > maxQuestionNum) {
                maxQuestionNum = currentQuestionNum;
            }
        }
        prevButton.setEnabled(true);
        prepareQuestion();
        showAnswerButton.setEnabled(false);
        setShowAnswerButtonEnabled();
    }

    private void prevQuestion() {
        saveUserAnswer();
        answersRadioGroup.clearCheck();
        if (currentQuestionNum == 0) {
            currentQuestionNum = questions.size() - 1;
        } else {
            currentQuestionNum--;
            prevButton.setEnabled(currentQuestionNum > 0
                    || maxQuestionNum == QuizUtil.QUESTIONS_COUNT - 1);
        }
        prepareQuestion();
        showAnswerButton.setEnabled(false);
        setShowAnswerButtonEnabled();
    }

    private void showRightAnswer() {
        saveUserAnswer();
        Question question = questions.get(currentQuestionNum);
        answersRadioGroup.removeAllViews();
        showAnswerButton.setEnabled(false);

        Integer userAnswerNum = userAnswers.get(currentQuestionNum);
        if (userAnswerNum != null) {
            String userAnswer = question.getAnswers().get(userAnswerNum);
            userAnswerText.setText(userAnswer);
            userAnswerLayout.setVisibility(View.VISIBLE);
            rightAnswerText.setText(question.getRightAnswerText());
            rightAnswerLayout.setVisibility(View.VISIBLE);

            questionText.setText(question.getImageDescription());
            if (userAnswer.equals(question.getRightAnswerText())) {
                userAnswerText.setTextColor(Color.GREEN);
            } else {
                userAnswerText.setTextColor(Color.RED);
            }

            showingAnswers.add(currentQuestionNum);
            if (showingAnswers.size() == QuizUtil.QUESTIONS_COUNT) {
                showAnswerButton.setVisibility(View.GONE);
                completeButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", QuizUtil.countRightAnswers(quiz, questions, userAnswers));
        intent.putExtra("spentTime", QuizUtil.countSpentTime(startTime));
        startActivity(intent);
    }

    private void startQuiz() {
        questions = QuizUtil.getRandomQuestions(quiz.getQuestions());

        completeButton.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.VISIBLE);
        prevButton.setEnabled(false);

        quiz.addAttempt();
        startTime = System.currentTimeMillis();
        currentQuestionNum = 0;
        maxQuestionNum = 0;
        userAnswers.clear();
        showingAnswers.clear();
        prepareQuestion();
        nextButton.setText("Next");
    }

    private void saveUserAnswer() {
        int answerId = answersRadioGroup.getCheckedRadioButtonId();
        if (answerId > -1) {
            userAnswers.put(currentQuestionNum, answerId);
        }
    }

    private void prepareQuestion() {
        Question question = questions.get(currentQuestionNum);

        image.setImageResource(question.getImage());
        currentQuestionNumText.setText(Integer.toString(currentQuestionNum + 1));
        questionsCountText.setText(Integer.toString(questions.size()));
        answersRadioGroup.removeAllViews();

        String userAnswer = null;
        Integer userAnswerNum = userAnswers.get(currentQuestionNum);
        if (userAnswerNum != null && question.getAnswers().size() > userAnswerNum) {
            userAnswer = question.getAnswers().get(userAnswerNum);
        }
        if (!showingAnswers.contains(currentQuestionNum)) {
            userAnswerLayout.setVisibility(View.GONE);
            rightAnswerLayout.setVisibility(View.GONE);
            questionText.setText(question.getQuestionText());

            for (String answer : question.getAnswers()) {
                RadioButton button = new RadioButton(this);
                button.setText(answer);
                button.setId(question.getAnswers().indexOf(answer));
                button.setChecked(userAnswer != null
                        && userAnswers.containsKey(currentQuestionNum)
                        && answer.equals(userAnswer));
                button.setTextSize(20);
                button.setTextColor(Color.rgb(61, 61, 61));
                button.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                answersRadioGroup.addView(button);
            }
        } else {
            userAnswerText.setText(userAnswer);
            userAnswerLayout.setVisibility(View.VISIBLE);
            rightAnswerText.setText(question.getRightAnswerText());
            rightAnswerLayout.setVisibility(View.VISIBLE);

            questionText.setText(question.getImageDescription());
            if (userAnswer.equals(question.getRightAnswerText())) {
                userAnswerText.setTextColor(Color.GREEN);
            } else {
                userAnswerText.setTextColor(Color.RED);
            }
        }
    }
}
