package com.booknest.library.service;

import com.booknest.library.dto.StudentSummary;
import com.booknest.library.entity.Borrowing;
import com.booknest.library.entity.Department;
import com.booknest.library.entity.Student;
import com.booknest.library.exception.DuplicateResourceException;
import com.booknest.library.exception.ResourceNotFoundException;
import com.booknest.library.repository.BorrowingRepository;
import com.booknest.library.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BorrowingRepository borrowingRepository;

    public StudentService(StudentRepository studentRepository, BorrowingRepository borrowingRepository) {
        this.studentRepository = studentRepository;
        this.borrowingRepository = borrowingRepository;
    }

    public Student create(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException("A student with email " + student.getEmail() + " already exists");
        }
        student.setId(null);
        if (student.getJoinDate() == null) {
            student.setJoinDate(LocalDate.now());
        }
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    public Student update(String id, Student updated) {
        Student existing = findById(id);
        studentRepository.findByEmail(updated.getEmail()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new DuplicateResourceException("A student with email " + updated.getEmail() + " already exists");
            }
        });
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setAge(updated.getAge());
        existing.setDepartment(updated.getDepartment());
        existing.setActive(updated.isActive());
        if (updated.getJoinDate() != null) {
            existing.setJoinDate(updated.getJoinDate());
        }
        return studentRepository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        borrowingRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
    }

    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> findByDepartment(Department department) {
        return studentRepository.findByDepartment(department);
    }

    public StudentSummary summary(String id) {
        Student student = findById(id);
        List<Borrowing> records = borrowingRepository.findByStudentId(id);
        long returned = records.stream().filter(Borrowing::isReturned).count();
        double fines = records.stream().mapToDouble(Borrowing::getFine).sum();
        return new StudentSummary(student.getId(), student.getName(),
                records.size(), returned, records.size() - returned, fines);
    }
}
