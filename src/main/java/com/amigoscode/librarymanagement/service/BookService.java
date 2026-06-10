package com.amigoscode.librarymanagement.service;
import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.exception.ResourceNotFoundException;
import com.amigoscode.librarymanagement.dto.CreateBookRequest;
import com.amigoscode.librarymanagement.dto.UpdateBookRequest;
import com.amigoscode.librarymanagement.entity.Book;
import com.amigoscode.librarymanagement.repository.BookRepository;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;


    public BookService(
            BookRepository bookRepository,
            BorrowingRepository borrowingRepository) {
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by("id").ascending());
    }
    public List<Book> searchBooksByTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new BadRequestException(
                    "Title search value is required"
            );
        }

        return bookRepository
                .findByTitleContainingIgnoreCaseOrderByIdAsc(
                        title.trim()
                );
    }
    public Book addBook( CreateBookRequest request) {
        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());
        return bookRepository.save(book);
    }

    public Book findBookById(Integer id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book with id:" + id + " was  not found"));
    }

    public Book addCopies(Integer quantity, Integer bookid) {
        Book book = bookRepository.findById(bookid).orElseThrow(() ->
                new ResourceNotFoundException("BOOK with id " + bookid + " was not found"));
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than 0"
            );
        }


        book.setTotalCopies(book.getTotalCopies() + quantity);
        book.setAvailableCopies(book.getAvailableCopies() + quantity);
        validateCopies(book);
        return bookRepository.save(book);

    }


    public void deleteBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("book with id:" + id + "isnt found"));
        // cant delete borrowed book

        boolean hasActiveBorrowing =
                borrowingRepository
                        .existsByBookIdAndReturnedFalse(id);

        if (hasActiveBorrowing) {
            throw new BadRequestException(
                    "Book with id " + id
                            + " cannot be deleted because it is currently borrowed"
            );
        }
        //cannt delete borrrowed book that we return it becouse it has history in borrowing
        boolean hasBorrowingHistory =
                borrowingRepository
                        .existsByBookId(id);

        if (hasBorrowingHistory) {
            throw new BadRequestException(
                    "Book with id " + id
                            + " cannot be deleted because borrowing history exists"
            );
        }

        bookRepository.deleteById(id);
    }

    private void validateCopies(Book book) {

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BadRequestException(
                    "Available copies cannot be greater than total copies"
            );
        }
    }

    public Book UpdateBookById(UpdateBookRequest request, Integer id) {
        Book existingBook = bookRepository.
                findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Book with id:" + id + " was  not found"));





        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setCategory(request.getCategory());

        return bookRepository.save(existingBook);


    }

    public Book removeCopies(Integer quantity, Integer bookid) {
        Book book = bookRepository.findById(bookid).orElseThrow(() ->
                new ResourceNotFoundException("BOOK with id " + bookid + " was not found"));
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than 0"
            );
        }
        if (quantity > book.getAvailableCopies()) {
            throw new BadRequestException(
                    "Cannot remove more copies than the available copies"
            );
        }

        book.setTotalCopies(book.getTotalCopies() - quantity);
        book.setAvailableCopies(book.getAvailableCopies() - quantity);
        validateCopies(book);
        return bookRepository.save(book);

    }

}