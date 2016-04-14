package com.example.user.geoquiz.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;
import com.example.user.geoquiz.model.QuizUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = this;
    private Quiz quiz;
    private int result;
    private long startTime;
    private int currentQuestionNum;
    private List<Question> questions;
    private Set<Integer> showingAnswers = new HashSet<>();
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    private Button nextButton;
    private Button prevButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        nextButton = (Button) findViewById(R.id.nextButton);
        prevButton = (Button) findViewById(R.id.prevButton);
        quiz = QuizUtil.loadQuiz(this);

        setClickListeners();
        prepareQuizInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
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
            case R.id.resetButton: {
                resetResults();
                break;
            }
        }
    }

    private void setClickListeners() {
        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.tryAgainButton).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);
        findViewById(R.id.prevButton).setOnClickListener(this);
        findViewById(R.id.mainMenuButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);
    }

    private void nextQuestion() {
        if (nextButton.getText().toString().equals("Next")) {
            saveUserAnswer();
            currentQuestionNum++;
            nextButton.setText(currentQuestionNum == questions.size() - 1 ? "Done" : "Next");
            prevButton.setEnabled(currentQuestionNum > 0);
            prepareQuestion();
        } else {
            saveUserAnswer();
            getResult();
            prepareResultInfo();

            ScrollView questionScroll = (ScrollView) findViewById(R.id.questionScroll);
            assert questionScroll != null;
            questionScroll.setVisibility(View.GONE);

            ViewGroup questionLayout = (ViewGroup) findViewById(R.id.questionLayout);
            assert questionLayout != null;
            questionLayout.setVisibility(View.GONE);

            ViewGroup resultLayout = (ViewGroup) findViewById(R.id.resultLayout);
            assert resultLayout != null;
            resultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void prevQuestion() {
        currentQuestionNum--;
        nextButton.setText("Next");
        prevButton.setEnabled(currentQuestionNum > 0);
        prepareQuestion();
    }

    private void showRightAnswer() {
        int rightAnswer = questions.get(currentQuestionNum).getRightAnswer();
        RadioButton radioButton = (RadioButton) findViewById(rightAnswer);
        assert radioButton != null;
        radioButton.setChecked(true);
        showingAnswers.add(currentQuestionNum);
    }

    private void goToMainMenu() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void resetResults() {
        quiz.setAttempts(0);
        quiz.setBestResult(0);
        QuizUtil.saveQuiz(quiz, context);
    }

    private void startQuiz() {
        questions = QuizUtil.getRandomQuestions(quiz.getQuestions());

        ViewGroup resultLayout = (ViewGroup) findViewById(R.id.resultLayout);
        assert resultLayout != null;
        resultLayout.setVisibility(View.GONE);

        ViewGroup startLayout = (ViewGroup) findViewById(R.id.startLayout);
        assert startLayout != null;
        startLayout.setVisibility(View.GONE);

        ViewGroup questionLayout = (ViewGroup) findViewById(R.id.questionLayout);
        assert questionLayout != null;
        questionLayout.setVisibility(View.VISIBLE);

        ScrollView questionScroll = (ScrollView) findViewById(R.id.questionScroll);
        assert questionScroll != null;
        questionScroll.setVisibility(View.VISIBLE);

        quiz.addAttempt();
        startTime = System.currentTimeMillis();
        currentQuestionNum = 0;
        result = 0;
        userAnswers.clear();
        showingAnswers.clear();
        prepareQuestion();
        nextButton.setText("Next");
    }

    private void saveUserAnswer() {
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
        quizAttempts.setText(Integer.toString(quiz.getAttempts()));

        TextView quizBestResult = (TextView) findViewById(R.id.quizBestResult);
        assert quizBestResult != null;
        quizBestResult.setText(Integer.toString(quiz.getBestResult()));
    }

    private void prepareQuestion() {
        Question question = questions.get(currentQuestionNum);
        String userAnswer = null;
        Integer userAnswerNum = userAnswers.get(currentQuestionNum);
        if (userAnswerNum != null && question.getAnswers().size() > userAnswerNum) {
            userAnswer = question.getAnswers().get(userAnswerNum);
        }

        ImageView image = (ImageView) findViewById(R.id.quizImage);
        assert image != null;
        image.setImageResource(question.getImage());

        TextView currentQuestionNum = (TextView) findViewById(R.id.currentQuestionNum);
        assert currentQuestionNum != null;
        currentQuestionNum.setText(Integer.toString(this.currentQuestionNum + 1));

        TextView questionsCount = (TextView) findViewById(R.id.questionsCount);
        assert questionsCount != null;
        questionsCount.setText(Integer.toString(questions.size()));

        TextView questionText = (TextView) findViewById(R.id.questionText);
        assert questionText != null;
        questionText.setText(question.getQuestionText());

        RadioGroup answersGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        assert answersGroup != null;
        answersGroup.removeAllViews();
        for (String answer : question.getAnswers()) {
            RadioButton button = new RadioButton(this);
            button.setText(answer);
            button.setId(question.getAnswers().indexOf(answer));
            button.setChecked(userAnswer != null
                    && userAnswers.containsKey(this.currentQuestionNum)
                    && answer.equals(userAnswer));
            answersGroup.addView(button);
        }
    }

    private void getResult() {
        for (Integer answer : showingAnswers) {
            userAnswers.remove(answer);
        }
        result = QuizUtil.countRightAnswers(quiz, userAnswers);
        QuizUtil.saveQuiz(quiz, this);
    }

    private void prepareResultInfo() {
        TextView message = (TextView) findViewById(R.id.message);
        assert message != null;
        message.setText(QuizUtil.generateMessage(result));

        TextView result = (TextView) findViewById(R.id.result);
        assert result != null;
        result.setText(Integer.toString(this.result) + " of " + QuizUtil.QUESTIONS_COUNT);

        TextView bestResult = (TextView) findViewById(R.id.bestResult);
        assert bestResult != null;
        bestResult.setText(Integer.toString(quiz.getBestResult()));

        TextView spentTime = (TextView) findViewById(R.id.spentTime);
        assert spentTime != null;
        bestResult.setText(QuizUtil.countSpentTime(startTime));
    }
}
