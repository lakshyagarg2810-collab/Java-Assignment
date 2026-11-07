package com.kiet.hostelmanagement.controller;

import com.kiet.hostelmanagement.entity.HostelStudent;
import com.kiet.hostelmanagement.repository.HostelStudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class HostelStudentController {

    @Autowired
    private HostelStudentRepository repository;


    // 1. ADD STUDENT

    @PostMapping
    public HostelStudent addStudent(
            @RequestBody HostelStudent student) {

        return repository.save(student);
    }


    // 2. GET ALL STUDENTS

    @GetMapping
    public List<HostelStudent> getAllStudents() {

        return repository.findAll();
    }


    // 3. GET STUDENT BY ID

    @GetMapping("/{id}")
    public ResponseEntity<HostelStudent> getStudentById(
            @PathVariable int id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // 4. UPDATE STUDENT DETAILS

    @PutMapping("/{id}")
    public ResponseEntity<HostelStudent> updateStudent(
            @PathVariable int id,
            @RequestBody HostelStudent newStudent) {

        return repository.findById(id)
                .map(student -> {

                    student.setHostelName(
                            newStudent.getHostelName());

                    student.setRoomNumber(
                            newStudent.getRoomNumber());

                    student.setFee(
                            newStudent.getFee());

                    HostelStudent updated =
                            repository.save(student);

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // 5. DELETE STUDENT

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable int id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            return ResponseEntity.ok(
                    "Student deleted successfully!");

        } else {

            return ResponseEntity.notFound().build();
        }
    }
}