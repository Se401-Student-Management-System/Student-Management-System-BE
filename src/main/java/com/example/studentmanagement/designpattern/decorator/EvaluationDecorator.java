package com.example.studentmanagement.designpattern.decorator;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;

public abstract class EvaluationDecorator implements EvaluationComponent {

    protected final EvaluationComponent component;

    protected EvaluationDecorator(EvaluationComponent component) {
        this.component = component;
    }

    @Override
    public void addComment(Score score, Teacher teacher, String comment) {
        component.addComment(score, teacher, comment);
    }
}