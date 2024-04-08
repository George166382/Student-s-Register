package com.example.catalog.entities;

import java.util.List;

public class Student {
	private String name;
    private List<Integer> marks;

    public Student() {
		// TODO Auto-generated constructor stub
	}

    public Student(String name, List<Integer> marks) {
		this.name = name;
		this.marks = marks;
	}
    

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Integer> getMarks() {
        return marks;
    }

    public void setMarks(List<Integer> marks) {
        this.marks = marks;
    }
}
