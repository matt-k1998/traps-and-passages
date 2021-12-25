package com.trap;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private List<Quiz> quizzes = new ArrayList<Quiz>();

    public List<Quiz> getQuestions() {
        return quizzes;
    }

    public void setQuestions(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
