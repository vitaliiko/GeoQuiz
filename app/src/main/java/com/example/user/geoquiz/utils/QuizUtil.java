package com.example.user.geoquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static int countRightAnswers(Quiz quiz, List<Question> questions, Map<Integer, Integer> userAnswers) {
        int result = 0;
        for (Integer questionNum : userAnswers.keySet()) {
            Integer rightAnswer = questions.get(questionNum).getRightAnswer();
            Integer userAnswer = userAnswers.get(questionNum);
            if (rightAnswer.equals(userAnswer)) {
                result++;
            }
        }
        quiz.setLastResult(result);
        if (result > quiz.getBestResult()) {
            quiz.setBestResult(result);
        }
        return result;
    }

    public static String generateMessage(int result) {
        switch (result) {
            case 1: return "BAD!";
            case 2: return "Not enough!";
            case 3: return "Not bad!";
            case 4: return "Nice!";
            case 5: return "Awesome!";
            default: return "Looser!";
        }
    }

    public static String countSpentTime(long startTime) {
        long currentTime = System.currentTimeMillis();
        long spentTime = Math.abs(currentTime - startTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(spentTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(spentTime) - minutes * 60;
        return minutes + " minutes, " + seconds + " seconds";
    }
}
