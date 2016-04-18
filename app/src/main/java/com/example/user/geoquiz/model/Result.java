package com.example.user.geoquiz.model;

public class Result {

    private int attempts;
    private int bestResult;

    public Result(int attempts, int bestResult) {
        this.attempts = attempts;
        this.bestResult = bestResult;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        return attempts == result.attempts && bestResult == result.bestResult;

    }

    @Override
    public int hashCode() {
        int result = attempts;
        result = 31 * result + bestResult;
        return result;
    }
}
