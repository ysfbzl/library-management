package com.amigoscode.librarymanagement.repository;

import com.amigoscode.librarymanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends
        JpaRepository<Member,Integer> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Integer id
    );

}
