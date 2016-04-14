package com.example.user.geoquiz.model;

import android.media.Image;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private int image;
    private String questionText;
    private List<String> answers;
    private int rightAnswer;

    public Question(int image, String questionText, List<String> answers, int rightAnswer) {
        this.image = image;
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
