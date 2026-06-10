package com.amigoscode.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;

@Entity
@Table(name="borrowings")
public class Borrowing {
    @Id
    @SequenceGenerator(
            name = "borrowing_id_sequence",
            sequenceName = "borrowing_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "borrowing_id_sequence"
    )
    private Integer id;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Book book;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private  Boolean returned;
    public  Borrowing(){

    }
    public Borrowing(
            Member member,
            Book book,
            LocalDate borrowDate,
            LocalDate returnDate,
            Boolean returned
    ) {
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }
}
