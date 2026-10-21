package com.booknest.library.repository;

import com.booknest.library.entity.Department;
import com.booknest.library.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByDepartment(Department department);
}
