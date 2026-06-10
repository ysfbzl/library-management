package com.amigoscode.librarymanagement.repository;
import java.util.List;

import com.amigoscode.librarymanagement.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository
        <Borrowing,Integer> {
    List<Borrowing> findByMemberIdOrderByIdAsc(Integer memberId);
    public List<Borrowing> findByReturnedFalseOrderByIdAsc();
    boolean existsByBookIdAndReturnedFalse(
            Integer bookId
    );
    boolean existsByMemberIdAndReturnedFalse(
            Integer memberd
    );
    boolean existsByMemberId(Integer id);
    boolean existsByBookId(
            Integer bookId
    );
}
