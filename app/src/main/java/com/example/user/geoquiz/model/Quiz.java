package com.example.user.geoquiz.model;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {

    private String name;
    private int mainImage;
    private String description;
    private List<Question> questions;
    private int attempts = 0;
    private int bestResult = 0;
    private int lastResult = 0;
    private String spentTime;

    public Quiz() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMainImage() {
        return mainImage;
    }

    public void setMainImage(int mainImage) {
        this.mainImage = mainImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getBestResult() {
        return bestResult;
    }

    public void setBestResult(int bestResult) {
        this.bestResult = bestResult;
    }

    public int getLastResult() {
        return lastResult;
    }

    public void setLastResult(int lastResult) {
        this.lastResult = lastResult;
    }

    public void addAttempt() {
        attempts++;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        return mainImage == quiz.mainImage
                && name.equals(quiz.name)
                && description.equals(quiz.description)
                && questions.equals(quiz.questions);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + mainImage;
        result = 31 * result + description.hashCode();
        result = 31 * result + questions.hashCode();
        return result;
    }
}
