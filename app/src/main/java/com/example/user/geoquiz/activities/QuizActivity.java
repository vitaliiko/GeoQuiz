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
import com.example.user.geoquiz.model.QuizUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private int result = 0;
    private long startTime;
    private int currentQuestionNum = 0;
    private List<Question> questions;
    private Set<Integer> showingAnswers = new HashSet<>();
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quiz = QuizUtil.loadQuiz(this);
        questions = QuizUtil.getRandomQuestions(quiz.getQuestions());

        prepareQuizInfo();
        addListenerOnStartButton();
        addListenerOnNavigationButtons();
        addListenerOnShowAnswerButton();
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
                quiz.addAttempt();
                startTime = System.currentTimeMillis();
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
                    saveUserAnswer();
                    currentQuestionNum++;
                    nextButton.setText(currentQuestionNum == questions.size() - 1 ? "Done" : "Next");
                    prevButton.setEnabled(currentQuestionNum > 0);
                    prepareQuestion();
                } else {
                    saveUserAnswer();
                    getResult();
                    prepareResultInfo();

                    ViewGroup questionLayout = (ViewGroup) findViewById(R.id.questionLayout);
                    assert questionLayout != null;
                    questionLayout.setVisibility(View.GONE);

                    ViewGroup resultLayout = (ViewGroup) findViewById(R.id.resultLayout);
                    assert resultLayout != null;
                    resultLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionNum--;
                nextButton.setText("Next");
                prevButton.setEnabled(currentQuestionNum > 0);
                prepareQuestion();
            }
        });
    }

    private void addListenerOnShowAnswerButton() {
        Button button = (Button) findViewById(R.id.showAnswerButton);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rightAnswer = questions.get(currentQuestionNum).getRightAnswer();
                RadioButton radioButton = (RadioButton) findViewById(rightAnswer);
                assert radioButton != null;
                radioButton.setChecked(true);
                showingAnswers.add(currentQuestionNum);
            }
        });
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
