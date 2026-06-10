package com.amigoscode.librarymanagement;

import com.amigoscode.librarymanagement.entity.Book;
import com.amigoscode.librarymanagement.entity.Borrowing;
import com.amigoscode.librarymanagement.entity.Member;
import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.exception.ResourceNotFoundException;
import com.amigoscode.librarymanagement.repository.BookRepository;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import com.amigoscode.librarymanagement.repository.MemberRepository;
import com.amigoscode.librarymanagement.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {
    @Mock
    BorrowingRepository borrowingRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BorrowingService borrowingService;
    @Test
    void shouldBorrowAvailableBookSuccessfully(){
        Member member =new Member();
        member.setId(1);
        member.setName("ALI");
        member.setEmail("ALI@GMAIL.COM");
        Book book = new Book(
                10,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                3,
                3
        );
        when(memberRepository.findById(1))
                .thenReturn(Optional.of(member));

        when(bookRepository.findById(10))
                .thenReturn(Optional.of(book));
        when(           borrowingRepository.save(
                        any(Borrowing.class)
                )
        ).thenAnswer(invocation ->
                invocation.getArgument(0)
        );
        Borrowing result=borrowingService.borrowBook(1,10);
        assertEquals(member,result.getMember());
        assertEquals(book,result.getBook());;
        assertEquals(2,book.getAvailableCopies());
        assertFalse(result.getReturned());
        verify(bookRepository).save(book);
        verify(borrowingRepository).save(any(Borrowing.class));


    }
    @Test
    void shouldRejectBorrowingWhenNoCopiesAreAvailable() {
        Member member = new Member();

        member.setId(1);
        member.setName("Ali");
        member.setEmail("ali@gmail.com");

        Book book = new Book(
                10,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                3,
                0
        );

        when(memberRepository.findById(1))
                .thenReturn(Optional.of(member));

        when(bookRepository.findById(10))
                .thenReturn(Optional.of(book));
        BadRequestException exception=assertThrows(BadRequestException.class,()->
                borrowingService.borrowBook(1,10) );
        assertEquals(
                "Book with id 10 has no available copies",
                exception.getMessage()
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(borrowingRepository, never())
                .save(any(Borrowing.class));
    }
    @Test
    void shouldReturnBorrowedBookSuccessfully() {
        Book book = new Book(
                10,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                3,
                1
        );
        Borrowing borrowing=new Borrowing();
        borrowing.setId(20);
        borrowing.setReturned(false);
        borrowing.setReturnDate(null);
        borrowing.setBook(book);
        when(borrowingRepository.findById(20)).thenReturn(Optional.of(borrowing));
        when(bookRepository.save(book)).thenReturn(book);
        when(borrowingRepository.save(borrowing)).thenReturn(borrowing);
        Borrowing result=borrowingService.returnBook(20);
        assertEquals(2,book.getAvailableCopies());
        assertTrue(result.getReturned());
        assertNotNull(result.getReturnDate());

        verify(bookRepository)
                .save(book);

        verify(borrowingRepository)
                .save(borrowing);
    }
    @Test
    void shouldRejectReturningSameBorrowingTwice() {

        Book book = new Book(
                10,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                3,
                2
        );

        Borrowing borrowing = new Borrowing();

        borrowing.setId(20);
        borrowing.setBook(book);
        borrowing.setReturned(true);
        borrowing.setReturnDate(LocalDate.now());

        when(borrowingRepository.findById(20))
                .thenReturn(Optional.of(borrowing));

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> borrowingService.returnBook(20)
                );

        assertEquals(
                "This book was already returned",
                exception.getMessage()
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(borrowingRepository, never())
                .save(any(Borrowing.class));
    }
    @Test
    void shouldRejectBorrowingWhenBookDoesNotExist() {

        Member member = new Member();

        member.setId(1);
        member.setName("Ali");
        member.setEmail("ali@gmail.com");

        when(memberRepository.findById(1))
                .thenReturn(Optional.of(member));

        when(bookRepository.findById(99))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> borrowingService.borrowBook(
                                1,
                                99
                        )
                );

        assertEquals(
                "book with id 99 was not found",
                exception.getMessage()
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(borrowingRepository, never())
                .save(any(Borrowing.class));
    }
}
