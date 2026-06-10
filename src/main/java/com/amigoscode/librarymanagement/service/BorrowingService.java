package com.amigoscode.librarymanagement.service;

import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.exception.ResourceNotFoundException;
import com.amigoscode.librarymanagement.entity.Book;
import com.amigoscode.librarymanagement.entity.Borrowing;
import com.amigoscode.librarymanagement.entity.Member;
import com.amigoscode.librarymanagement.repository.BookRepository;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import com.amigoscode.librarymanagement.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BorrowingService(BorrowingRepository borrowingRepository,
                            MemberRepository memberRepository,
                            BookRepository bookRepository) {
        this.borrowingRepository = borrowingRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Borrowing borrowBook(Integer memberId, Integer bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("member id " + memberId + "not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("book with id " + bookId + " was not found"));
        if (book.getAvailableCopies() == null || book.getAvailableCopies()<= 0) {
            throw new BadRequestException(
                    "Book with id " + bookId + " has no available copies"
            );
        }

        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);
        Borrowing borrowing = new Borrowing(
                member, book, LocalDate.now(), null, false);
        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public Borrowing returnBook(Integer borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId).
                orElseThrow(() -> new ResourceNotFoundException("borrowing id:" + borrowingId + "not found"));
        if (Boolean.TRUE.equals(borrowing.getReturned())) {
            throw new BadRequestException(
                    "This book was already returned"
            );
        }
            borrowing.setReturned(true);
            borrowing.setReturnDate(LocalDate.now());
            Book book = borrowing.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            bookRepository.save(book);
            return borrowingRepository.save(borrowing);

        }
    public  List<Borrowing> getAllBorrowing(){
        return  borrowingRepository.findAll(Sort.by("id").ascending());
    }
    public  List<Borrowing> getBorrowingByMemberId(Integer memberid){
        memberRepository
                .findById(memberid)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member with id " + memberid + " was not found"
                        )
                );
        return  borrowingRepository.findByMemberIdOrderByIdAsc(memberid);
    }
    public  List <Borrowing> getActiveBorrowings(){
        return  borrowingRepository.findByReturnedFalseOrderByIdAsc();
    }
}
