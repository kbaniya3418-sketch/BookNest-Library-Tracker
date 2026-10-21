package com.booknest.library.dto;

public record StudentSummary(
        String studentId,
        String studentName,
        long totalBorrowed,
        long returnedBooks,
        long pendingBooks,
        double totalFine) {
}
