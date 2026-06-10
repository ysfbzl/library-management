package com.amigoscode.librarymanagement.controller;

import com.amigoscode.librarymanagement.entity.Borrowing;
import com.amigoscode.librarymanagement.service.BorrowingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/borrowings")
public class BorrowingController {
    private  final BorrowingService borrowingService;
    public  BorrowingController(BorrowingService borrowingService){
        this.borrowingService=borrowingService;
    }

    @PostMapping("borrow/member/{memberId}/book/{bookId}")
    public Borrowing borrowBook(
            @PathVariable Integer memberId,
            @PathVariable Integer bookId
    ) {
        return borrowingService.borrowBook(memberId, bookId);
    }
    @PutMapping("{borrowingId}/return")
    public Borrowing returnBook(
            @PathVariable Integer borrowingId
    ) {
        return borrowingService.returnBook(borrowingId);
    }
    @GetMapping
    public List<Borrowing> getBorrowing(){
        return  borrowingService.getAllBorrowing();
    }
    @GetMapping("member/{memberId}")
    public  List<Borrowing> getBorrowingByMemberId(@PathVariable Integer memberId){
        return  borrowingService.getBorrowingByMemberId(memberId);
    }
    @GetMapping("active")
    public  List <Borrowing> getActiveBorrowings(){
        return  borrowingService.getActiveBorrowings();
    }
}
