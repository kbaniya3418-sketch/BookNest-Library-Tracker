package com.booknest.library.controller;

import com.booknest.library.entity.Borrowing;
import com.booknest.library.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<Borrowing> create(@Valid @RequestBody Borrowing borrowing) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowingService.create(borrowing));
    }

    @GetMapping
    public List<Borrowing> getAll() {
        return borrowingService.findAll();
    }

    @GetMapping("/overdue")
    public List<Borrowing> overdue() {
        return borrowingService.findOverdue();
    }

    @GetMapping("/{id}")
    public Borrowing getOne(@PathVariable String id) {
        return borrowingService.findById(id);
    }

    @PutMapping("/{id}")
    public Borrowing update(@PathVariable String id, @Valid @RequestBody Borrowing borrowing) {
        return borrowingService.update(id, borrowing);
    }

    @PutMapping("/{id}/return")
    public Borrowing returnBook(@PathVariable String id) {
        return borrowingService.returnBook(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        borrowingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public List<Borrowing> byStudent(@PathVariable String studentId) {
        return borrowingService.findByStudent(studentId);
    }

    @GetMapping("/genre/{genre}")
    public List<Borrowing> byGenre(@PathVariable String genre) {
        return borrowingService.findByGenre(genre);
    }
}
