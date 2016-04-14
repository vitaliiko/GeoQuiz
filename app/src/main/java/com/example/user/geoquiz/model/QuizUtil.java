package com.example.user.geoquiz.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.geoquiz.R;
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

    public static void prepareQuiz(Context context) {
        Quiz quiz = new Quiz("Quiz", "sdsdfs sdfsdf sdfsdf", getQuestions());
        saveQuiz(quiz, context);
    }

    public static void saveQuiz(Quiz quiz, Context context) {
        SharedPreferences appSharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(quiz);
        prefsEditor.putString("quiz", json);
        prefsEditor.apply();
    }

    public static Quiz loadQuiz(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String json = preferences.getString("quiz", "");
        return new Gson().fromJson(json, Quiz.class);
    }

    private static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(R.drawable.image1, "Question1", getAnswers(4), 0));
        questions.add(new Question(R.drawable.image2, "Question2", getAnswers(5), 2));
        questions.add(new Question(R.drawable.image3, "Question3", getAnswers(3), 1));
        questions.add(new Question(R.drawable.image4, "Question4", getAnswers(4), 3));
        questions.add(new Question(R.drawable.image5, "Question5", getAnswers(3), 2));
        questions.add(new Question(R.drawable.image6, "Question6", getAnswers(4), 0));
        questions.add(new Question(R.drawable.image7, "Question7", getAnswers(5), 2));
        questions.add(new Question(R.drawable.image8, "Question8", getAnswers(3), 1));
        questions.add(new Question(R.drawable.image9, "Question9", getAnswers(4), 3));
        questions.add(new Question(R.drawable.image10, "Question10", getAnswers(3), 2));
        return questions;
    }

    private static List<String> getAnswers(int count) {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            answers.add("answer" + i);
        }
        return answers;
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

    public static int countRightAnswers(Quiz quiz, Map<Integer, Integer> userAnswers) {
        int result = 0;
        for (Integer questionNum : userAnswers.keySet()) {
            if (quiz.getQuestions().get(questionNum).getRightAnswer() == userAnswers.get(questionNum)) {
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
