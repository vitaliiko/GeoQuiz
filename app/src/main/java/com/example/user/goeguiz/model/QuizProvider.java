package com.example.user.goeguiz.model;

import java.util.ArrayList;
import java.util.List;

public class QuizProvider {

    public static Quiz prepareQuiz() {
        return new Quiz("Quiz", "sdsdfs sdfsdf sdfsdf", getQuestions());
    }

    private static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Question", getAnswers(4), 0));
        questions.add(new Question("Question", getAnswers(5), 2));
        questions.add(new Question("Question", getAnswers(3), 1));
        questions.add(new Question("Question", getAnswers(4), 3));
        questions.add(new Question("Question", getAnswers(3), 2));
        return questions;
    }

    private static List<String> getAnswers(int count) {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            answers.add("answer" + i);
        }
        return answers;
    }
}
