package com.example.user.geoquiz.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizUtil {

    public static Quiz prepareQuiz() {
        return new Quiz("Quiz", "sdsdfs sdfsdf sdfsdf", getQuestions());
    }

    private static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Question1", getAnswers(4), 0));
        questions.add(new Question("Question2", getAnswers(5), 2));
        questions.add(new Question("Question3", getAnswers(3), 1));
        questions.add(new Question("Question4", getAnswers(4), 3));
        questions.add(new Question("Question5", getAnswers(3), 2));
        questions.add(new Question("Question6", getAnswers(4), 0));
        questions.add(new Question("Question7", getAnswers(5), 2));
        questions.add(new Question("Question8", getAnswers(3), 1));
        questions.add(new Question("Question9", getAnswers(4), 3));
        questions.add(new Question("Question10", getAnswers(3), 2));
        return questions;
    }

    private static List<String> getAnswers(int count) {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            answers.add("answer" + i);
        }
        return answers;
    }

    public static List<Question> getRandomQuestions(int count, List<Question> questionsList) {
        Set<Question> questionsSet = null;
        try {
            questionsSet = new HashSet<>();
            if (questionsList.size() < count) {
                throw new IOException("");
            }
            while (questionsSet.size() < count) {
                int randomIndex = (int) (Math.random() * questionsList.size());
                questionsSet.add(questionsList.get(randomIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(questionsSet);
    }
}
