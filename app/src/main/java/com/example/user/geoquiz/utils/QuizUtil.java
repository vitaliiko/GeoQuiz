package com.example.user.geoquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.geoquiz.R;
import com.example.user.geoquiz.model.Question;
import com.example.user.geoquiz.model.Quiz;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class QuizUtil {

    public static final int QUESTIONS_COUNT = 5;

    public static void saveQuiz(Quiz quiz, Context context) {
        SharedPreferences appSharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(quiz);
        prefsEditor.putString("quiz", json);
        prefsEditor.apply();
    }

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
                throw new IOException("");
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
        return minutes + context.getString(R.string.minutes) +
                seconds + context.getString(R.string.seconds);
    }
}
