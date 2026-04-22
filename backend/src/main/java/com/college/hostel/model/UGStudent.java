package com.college.hostel.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "ug_students")
public class UGStudent extends Student {

    private String branch;

    @Column(name = "ug_year")
    private int ugYear;

    public UGStudent() {}

    public UGStudent(String branch, int ugYear) {
        this.branch = branch;
        this.ugYear = ugYear;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getUgYear() {
        return ugYear;
    }

    public void setUgYear(int ugYear) {
        this.ugYear = ugYear;
    }
}
