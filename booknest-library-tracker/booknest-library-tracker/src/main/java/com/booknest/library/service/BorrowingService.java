package com.booknest.library.service;

import com.booknest.library.entity.Borrowing;
import com.booknest.library.exception.ResourceNotFoundException;
import com.booknest.library.repository.BorrowingRepository;
import com.booknest.library.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowingService {

    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;

    private final BorrowingRepository borrowingRepository;
    private final StudentRepository studentRepository;

    public BorrowingService(BorrowingRepository borrowingRepository, StudentRepository studentRepository) {
        this.borrowingRepository = borrowingRepository;
        this.studentRepository = studentRepository;
    }

    private void checkStudentExists(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
    }

    public Borrowing create(Borrowing borrowing) {
        checkStudentExists(borrowing.getStudentId());
        borrowing.setId(null);
        if (borrowing.getBorrowDate() == null) {
            borrowing.setBorrowDate(LocalDate.now());
        }
        if (borrowing.getDueDate() == null) {
            borrowing.setDueDate(borrowing.getBorrowDate().plusDays(LOAN_DAYS));
        }
        borrowing.setReturned(false);
        borrowing.setReturnDate(null);
        borrowing.setFine(0);
        return borrowingRepository.save(borrowing);
    }

    public List<Borrowing> findAll() {
        return borrowingRepository.findAll();
    }

    public Borrowing findById(String id) {
        return borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id " + id));
    }

    public Borrowing update(String id, Borrowing updated) {
        Borrowing existing = findById(id);
        checkStudentExists(updated.getStudentId());
        existing.setStudentId(updated.getStudentId());
        existing.setBookTitle(updated.getBookTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setGenre(updated.getGenre());
        if (updated.getBorrowDate() != null) existing.setBorrowDate(updated.getBorrowDate());
        if (updated.getDueDate() != null) existing.setDueDate(updated.getDueDate());
        existing.setReturned(updated.isReturned());
        existing.setReturnDate(updated.getReturnDate());
        existing.setFine(updated.getFine());
        return borrowingRepository.save(existing);
    }

    /** Marks a book as returned and calculates the late fine (Rs. 5 per overdue day). */
    public Borrowing returnBook(String id) {
        Borrowing b = findById(id);
        if (b.isReturned()) {
            return b;
        }
        LocalDate today = LocalDate.now();
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(b.getDueDate(), today));
        b.setReturned(true);
        b.setReturnDate(today);
        b.setFine(lateDays * FINE_PER_DAY);
        return borrowingRepository.save(b);
    }

    public void delete(String id) {
        findById(id);
        borrowingRepository.deleteById(id);
    }

    public List<Borrowing> findByStudent(String studentId) {
        checkStudentExists(studentId);
        return borrowingRepository.findByStudentId(studentId);
    }

    public List<Borrowing> findByGenre(String genre) {
        return borrowingRepository.findByGenreIgnoreCase(genre);
    }

    public List<Borrowing> findOverdue() {
        return borrowingRepository.findByReturnedFalseAndDueDateBefore(LocalDate.now());
    }
}
