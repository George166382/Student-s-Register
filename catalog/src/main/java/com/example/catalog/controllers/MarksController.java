package com.example.catalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalog.entities.Student;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class MarksController {
 
    private List<Student> students;
  
    @Autowired
    public MarksController() throws IOException, CsvException {
    	
    	
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("students.csv").getInputStream()))) {
            List<String[]> csvData = csvReader.readAll();
            students = csvData.stream()
                    .map(this::createStudentFromCsvRow)
                    .collect(Collectors.toCollection(ArrayList::new)); 
        }
    }

    private Student createStudentFromCsvRow(String[] csvRow) {
        Student student = new Student();
        student.setName(csvRow[0]);
        List<Integer> marks = List.of(csvRow).subList(1, csvRow.length).stream()
                .map(Integer::parseInt)
                .toList();
        student.setMarks(marks);
        return student;
    }
    
    @GetMapping
    public List<Student> getAllStudents() {
    	for(Student st : students) {
    		System.out.println(st.getMarks());
    	}
    	
        return students;
    }

    @PostMapping
    public void createStudent(@RequestBody Student newStudent) {
        try {
            
            students.add(newStudent);
            System.out.println("New Student Added: " + newStudent);
            updateCsvFile();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }


    @PutMapping("/{name}")
    public void updateStudentMarks(@PathVariable String name, @RequestBody Student updatedStudent) throws IOException, CsvException {
       
        int ok=0;
        for(Student studen : students) {
        	if(studen.getName().equals(updatedStudent.getName())) {
        		ok=1;
        		studen.setMarks(updatedStudent.getMarks());
        		break;
        		
        	}
        }
        if(ok==0) {
        	 // Handle case where student with given name does not exist
            throw new IllegalArgumentException("Student with name " + name + " not found.");
        }
       
         else {
           updateCsvFile();
        }
    }


    @DeleteMapping("/{name}")
    public void deleteStudent(@RequestParam String name) throws IOException, CsvException {
        
        students.removeIf(student -> student.getName().equals(name));

        updateCsvFile();
    }
    
    
    private void updateCsvFile() throws IOException {
        String filePath = "src/main/resources/students.csv";

        // Write the updated student data to a temporary buffer
        StringWriter sw = new StringWriter();
        try (BufferedWriter writer = new BufferedWriter(sw)) {
            for (Student student : students) {
            	System.out.println(student.getMarks());
                writer.write(String.join(",", createCsvRowFromStudent(student)));
                writer.newLine();
            }
        }

        // Replace the content of the original file with the updated data
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            fileWriter.write(sw.toString());
        }
    }

    
    private String[] createCsvRowFromStudent(Student student) {
        List<String> csvRow = new ArrayList<>();
        csvRow.add(student.getName());

        List<Integer> marks = student.getMarks();
        if (marks != null) {
            csvRow.addAll(marks.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
        }

        return csvRow.toArray(new String[0]);
    }

}
