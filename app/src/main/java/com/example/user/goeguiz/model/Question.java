package com.example.user.goeguiz.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private String questionText;
    private List<String> answers;
    private int rightAnswer;

    public Question(String questionText, List<String> answers, int rightAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
