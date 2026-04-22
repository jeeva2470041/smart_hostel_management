package com.college.hostel.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mba_students")
public class MBAStudent extends Student {
    private String stream;
    private int trimester;

    public MBAStudent() {}

    public MBAStudent(String stream, int trimester) {
        this.stream = stream;
        this.trimester = trimester;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public int getTrimester() {
        return trimester;
    }

    public void setTrimester(int trimester) {
        this.trimester = trimester;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonProperty("studentType")
    public String getStudentType() {
        return "MBA";
    }
}