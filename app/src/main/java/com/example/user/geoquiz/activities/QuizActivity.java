package com.example.user.geoquiz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.model.Result;
import com.example.user.geoquiz.utils.QuizUtil;
import com.google.gson.Gson;

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
        nextButton = (Button) findViewById(R.id.btn_next);
        prevButton = (Button) findViewById(R.id.btn_prev);
        showAnswerButton = (Button) findViewById(R.id.btn_show_answer);
        completeButton = (Button) findViewById(R.id.btn_complete);

        userAnswerText = (TextView) findViewById(R.id.text_user_answer);
        rightAnswerText = (TextView) findViewById(R.id.text_right_answer);
        questionText = (TextView) findViewById(R.id.text_question);
        currentQuestionNumText = (TextView) findViewById(R.id.text_current_question_num);
        questionsCountText = (TextView) findViewById(R.id.text_question_count);

        answersRadioGroup = (RadioGroup) findViewById(R.id.radio_gr_answers);
        image = (ImageView) findViewById(R.id.img_quiz);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_try_again: {
                startQuiz();
                break;
            }
            case R.id.btn_next: {
                nextQuestion();
                break;
            }
            case R.id.btn_prev: {
                prevQuestion();
                break;
            }
            case R.id.btn_show_answer: {
                showRightAnswer();
                break;
            }
            case R.id.btn_main_menu: {
                goToMainMenu();
                break;
            }
            case R.id.btn_complete: {
                saveResults();
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
            showAnswer(userAnswer, question.getRightAnswerText());
            questionText.setText(question.getImageDescription());

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
        intent.putExtra(Quiz.class.getSimpleName(), quiz);
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
            userAnswerText.setVisibility(View.GONE);
            rightAnswerText.setVisibility(View.GONE);
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
            showAnswer(userAnswer, question.getRightAnswerText());
            questionText.setText(question.getImageDescription());
        }
    }

    private void showAnswer(String userAnswer, String rightAnswer) {
        userAnswerText.setText(getString(R.string.your_answer) + " " + userAnswer);
        userAnswerText.setVisibility(View.VISIBLE);
        rightAnswerText.setText(getString(R.string.right_answer) + " " + rightAnswer);
        rightAnswerText.setVisibility(View.VISIBLE);

        if (userAnswer.equals(rightAnswer)) {
            userAnswerText.setTextColor(Color.GREEN);
        } else {
            userAnswerText.setTextColor(Color.RED);
        }
    }

    private void saveResults() {
        int result = QuizUtil.countRightAnswers(questions, userAnswers);
        String spentTime = QuizUtil.countSpentTime(startTime, this);

        quiz.setLastResult(result);
        quiz.setSpentTime(spentTime);
        if (result > quiz.getBestResult()) {
            quiz.setBestResult(result);
        }

        Result resultObject = new Result(quiz.getAttempts(), quiz.getBestResult());
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(quiz.getName(), new Gson().toJson(resultObject));
        editor.commit();
    }
}
