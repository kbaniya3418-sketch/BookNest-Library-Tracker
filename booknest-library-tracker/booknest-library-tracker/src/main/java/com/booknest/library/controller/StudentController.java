package com.booknest.library.controller;

import com.booknest.library.dto.StudentSummary;
import com.booknest.library.entity.Department;
import com.booknest.library.entity.Student;
import com.booknest.library.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(student));
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student getOne(@PathVariable String id) {
        return studentService.findById(id);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable String id, @Valid @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Student> search(@RequestParam String name) {
        return studentService.searchByName(name);
    }

    @GetMapping("/department/{department}")
    public List<Student> byDepartment(@PathVariable Department department) {
        return studentService.findByDepartment(department);
    }

    @GetMapping("/{id}/summary")
    public StudentSummary summary(@PathVariable String id) {
        return studentService.summary(id);
    }
}
