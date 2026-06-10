package com.amigoscode.librarymanagement;

import com.amigoscode.librarymanagement.dto.CreateMemberRequest;
import com.amigoscode.librarymanagement.entity.Member;
import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import com.amigoscode.librarymanagement.repository.MemberRepository;
import com.amigoscode.librarymanagement.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void shouldNormalizeEmailWhenAddingMember() {

        CreateMemberRequest request =
                new CreateMemberRequest();

        request.setName("ALI");
        request.setEmail("   ALI@GMAIL.COM   ");

        when(
                memberRepository
                        .existsByEmailIgnoreCase(
                                "ali@gmail.com"
                        )
        ).thenReturn(false);

        when(memberRepository.save(any(Member.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Member result =
                memberService.addMember(request);

        assertEquals(
                "ali@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "ALI",
                result.getName()
        );

        verify(memberRepository)
                .save(any(Member.class));
    }

    @Test
    void shouldRejectDuplicateEmailWhenAddingMember() {

        CreateMemberRequest request =
                new CreateMemberRequest();

        request.setName("ALI");
        request.setEmail("   ALI@GMAIL.COM   ");

        when(
                memberRepository
                        .existsByEmailIgnoreCase(
                                "ali@gmail.com"
                        )
        ).thenReturn(true);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> memberService.addMember(request)
                );

        assertEquals(
                "Email ali@gmail.com already exists",
                exception.getMessage()
        );

        verify(memberRepository, never())
                .save(any(Member.class));
    }

    @Test
    void shouldRejectUpdatingMemberWithAnotherExistingEmail() {

        Member existingMember =
                new Member();

        existingMember.setId(1);
        existingMember.setName("YOUSSEF");
        existingMember.setEmail("youssef@gmail.com");

        CreateMemberRequest request =
                new CreateMemberRequest();

        request.setName("ALI");
        request.setEmail("   ALI@GMAIL.COM   ");

        when(memberRepository.findById(1))
                .thenReturn(Optional.of(existingMember));

        when(
                memberRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                "ali@gmail.com",
                                1
                        )
        ).thenReturn(true);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> memberService.updateMemberById(
                                1,
                                request
                        )
                );

        assertEquals(
                "Email ali@gmail.com already exists",
                exception.getMessage()
        );

        verify(memberRepository, never())
                .save(any(Member.class));
    }
    void shouldRejectDeletingMemberWithBorrowingHistory(){
           Member member=new Member();
           member.setEmail("ALI@GMAIL.COM");
           member.setName("ALI");
           member.setId(1);
           when(memberRepository.findById(1)).thenReturn(Optional.of(member));
           when(borrowingRepository.existsByBookIdAndReturnedFalse(1)).thenReturn(false);
           when(borrowingRepository.existsByMemberId(1)).thenReturn(true);
        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> memberService.deleteMemberById(1)
                );

        assertEquals(
                "Member with id 1 cannot be deleted because borrowing history exists",
                exception.getMessage()
        );

        verify(memberRepository, never())
                .delete(any(Member.class));
    }


}