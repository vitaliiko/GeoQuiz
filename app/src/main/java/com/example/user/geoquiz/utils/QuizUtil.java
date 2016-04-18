package com.example.user.geoquiz.utils;

import android.content.Context;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class QuizUtil {

    public static final int QUESTIONS_COUNT = 5;

    public static Quiz loadQuiz(Context context, int resourceFileId) {
        Quiz quiz = new Quiz();
        quiz.setQuestions(ContentLoader.loadContent(context, resourceFileId));
        return quiz;
    }

    public static List<Question> getRandomQuestions(List<Question> questionsList) {
        Set<Question> questionsSet = null;
        try {
            questionsSet = new HashSet<>();
            if (questionsList.size() < QUESTIONS_COUNT) {
                throw new IOException("Questions count must not be less then " + QUESTIONS_COUNT);
            }
            while (questionsSet.size() < QUESTIONS_COUNT) {
                int randomIndex = (int) (Math.random() * questionsList.size());
                questionsSet.add(questionsList.get(randomIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(questionsSet);
    }

    public static int countRightAnswers(List<Question> questions,
                                        Map<Integer, Integer> userAnswers) {

        int result = 0;
        for (Integer questionNum : userAnswers.keySet()) {
            Integer rightAnswer = questions.get(questionNum).getRightAnswer();
            Integer userAnswer = userAnswers.get(questionNum);
            if (rightAnswer.equals(userAnswer)) {
                result++;
            }
        }
        return result;
    }

    public static int generateMessage(int result) {
        switch (result) {
            case 1: return R.string.bad;
            case 2: return R.string.not_enough;
            case 3: return R.string.not_bad;
            case 4: return R.string.nice;
            case 5: return R.string.awesome;
            default: return R.string.looser;
        }
    }

    public static String countSpentTime(long startTime, Context context) {
        long currentTime = System.currentTimeMillis();
        long spentTime = Math.abs(currentTime - startTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(spentTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(spentTime) - minutes * 60;
        return minutes + " " + context.getString(R.string.minutes) + ", " +
                seconds + " " + context.getString(R.string.seconds);
    }
}
