
package com.college.hostel.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pg_students")
public class PGStudent extends Student {
    private String specialization;
    private int semester;

    public PGStudent() {}

    public PGStudent(String specialization, int semester) {
        this.specialization = specialization;
        this.semester = semester;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonProperty("studentType")
    public String getStudentType() {
        return "PG";
    }
}