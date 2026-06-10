package com.amigoscode.librarymanagement.controller;
import com.amigoscode.librarymanagement.dto.CreateBookRequest;
import com.amigoscode.librarymanagement.dto.UpdateBookRequest;
import com.amigoscode.librarymanagement.entity.Book;
import com.amigoscode.librarymanagement.service.BookService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
  @RestController
  @RequestMapping("api/v1/books")
  public class BookController {
 private  final BookService bookService;

 public BookController(BookService bookService){
     this.bookService=bookService;
 }


 @GetMapping
    public List<Book> getBooks(){
     return  bookService.getAllBooks();
 }


 @PostMapping
 public  Book addbook( @Valid @RequestBody CreateBookRequest request){
     return  bookService.addBook(request);
 }
 @GetMapping("/search")
 public List<Book> searchBooksByTitle(
              @RequestParam String title
      ) {
          return bookService.searchBooksByTitle(title);
      }

 @GetMapping("{id}")
    public  Book findBook( @PathVariable Integer id){

     return  bookService.findBookById(id);
}
    @DeleteMapping("{id}")
    public  void deleteBook( @PathVariable Integer id){

       bookService.deleteBookById(id);
    }
    @PutMapping ("{id}")
    public  Book UpdateBook( @Valid @RequestBody UpdateBookRequest updatedbook, @PathVariable Integer id){

        return  bookService.UpdateBookById(updatedbook,id);
    }
    @PutMapping("/{bookid}/copies/add")
    public  Book addCopies(@PathVariable Integer bookid,@RequestParam Integer quantity){
     return  bookService.addCopies(quantity,bookid);
    }
    @PutMapping("/{bookid}/copies/remove")
    public  Book removeCopies(@PathVariable Integer bookid,@RequestParam Integer quantity){
        return  bookService.removeCopies(quantity,bookid);
    }

}
