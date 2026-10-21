package com.booknest.library.repository;

import com.booknest.library.entity.Borrowing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRepository extends MongoRepository<Borrowing, String> {
    List<Borrowing> findByStudentId(String studentId);
    List<Borrowing> findByGenreIgnoreCase(String genre);
    List<Borrowing> findByReturnedFalseAndDueDateBefore(LocalDate date);
    void deleteByStudentId(String studentId);
}
