package com.amigoscode.librarymanagement;

import com.amigoscode.librarymanagement.entity.Book;
import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.repository.BookRepository;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import com.amigoscode.librarymanagement.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldAddCopiesSuccessfully() {

        Book book = new Book(
                1,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                5,
                3
        );
//when used:the fake repository doesnt know what to return so we teach
        //it so when it find this id book retrurn it if exist
        when(bookRepository.findById(1))
                .thenReturn(Optional.of(book));

        when(bookRepository.save(book))
                .thenReturn(book);

        Book result =
                bookService.addCopies(2, 1);

        assertEquals(7, result.getTotalCopies());
        assertEquals(5, result.getAvailableCopies());

        verify(bookRepository).save(book);
    }

    @Test
        //why do we create object here since in the main method we make bookRepository.findById(bookId)
        //and sinvce it fails when questinty is 0
    void shouldRejectAddingZeroCopies() {

        Book book = new Book(
                1,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                5,
                3
        );

        when(bookRepository.findById(1))
                .thenReturn(Optional.of(book));

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> bookService.addCopies(0, 1)
                );

        assertEquals(
                "Quantity must be greater than 0",
                exception.getMessage()
        );

        verify(bookRepository, never())
                .save(any(Book.class));
    }

    @Test
    void ShouldRemoveCopiesSuccessfully() {
        Book book = new Book(
                1,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                5,
                3
        );
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        Book result = bookService.removeCopies(2, 1);
        assertEquals(3, result.getTotalCopies());
        assertEquals(1, result.getAvailableCopies());
        verify(bookRepository).save(book);
    }

    @Test
    void shouldRejectRemovingMoreCopiesThanAvailable() {

        Book book = new Book(
                1,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                5,
                2
        );
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        BadRequestException exception =
                assertThrows(BadRequestException.class,
                        () -> bookService.removeCopies(3, 1));
        assertEquals("Cannot remove more copies than the available copies",exception.getMessage());
        verify(bookRepository,never()).save(any(Book.class));

    }
    //reject delteecurrently borrowed book
    @Test
    void shouldRejectDeletingCurrentlyBorrowedBook() {

        Book book = new Book(
                1,
                "Spring Boot Basics",
                "Test Author",
                "Programming",
                5,
                2
        );
        //how it works when we call deltebookbyid to service and we did borrowingRepository.existsByBookIdAndReturnedFalse(1)).thenReturn(true);
        //so in service hasActiveBorrowing become true so it raise exception
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(borrowingRepository.existsByBookIdAndReturnedFalse(1)).thenReturn(true);
        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> bookService.deleteBookById(1)
                );
        assertEquals(
                "Book with id " + 1
                        + " cannot be deleted because it is currently borrowed",
                exception.getMessage()
        );

        verify(bookRepository, never())
                .deleteById(1);
    }
}