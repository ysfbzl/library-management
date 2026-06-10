package com.amigoscode.librarymanagement.repository;
import java.util.List;

import com.amigoscode.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository  extends JpaRepository<Book,Integer> {
    List<Book> findByTitleContainingIgnoreCaseOrderByIdAsc(
            String title
    );
}
