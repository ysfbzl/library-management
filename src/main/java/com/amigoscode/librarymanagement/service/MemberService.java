package com.amigoscode.librarymanagement.service;

import com.amigoscode.librarymanagement.exception.BadRequestException;
import com.amigoscode.librarymanagement.exception.ResourceNotFoundException;
import com.amigoscode.librarymanagement.entity.Member;
import com.amigoscode.librarymanagement.repository.BorrowingRepository;
import com.amigoscode.librarymanagement.repository.MemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.amigoscode.librarymanagement.dto.CreateMemberRequest;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private  final BorrowingRepository borrowingRepository;

    public MemberService(MemberRepository memberRepository,BorrowingRepository borrowingRepository) {
        this.memberRepository = memberRepository;
        this.borrowingRepository=borrowingRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll(Sort.by("id").ascending());
    }
    private void validateEmailDoesNotExist(String email) {

        String normalizedEmail = email.trim().toLowerCase();

        if (memberRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BadRequestException(
                    "Email " + normalizedEmail + " already exists"
            );
        }
    }
    public Member addMember(CreateMemberRequest request) {

        String normalizedEmail =
               request.getEmail().trim().toLowerCase();

        validateEmailDoesNotExist(normalizedEmail);


        Member member=new Member();
        member.setName(request.getName());
        member.setEmail(normalizedEmail);

        return memberRepository.save(member);
    }


    public Member findMemberById(Integer id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member with id " + id + " was not found"
                        )
                );
    }

    public void deleteMemberById(Integer id) {
        Member member = memberRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member with id: " + id + " was not found"
                        )
                );
          //if he is borrowing we cant delete it
        Boolean isactive=borrowingRepository.existsByMemberIdAndReturnedFalse(id);
        if(isactive){
            throw new BadRequestException("Member with id " + id + " cannot be deleted because they have an active borrowing");
        }
        //if he just borrowed even one time so he has history'
        boolean hasborrowHistory=borrowingRepository.existsByMemberId(id);
        if(hasborrowHistory){
            throw new BadRequestException(
                    "Member with id " + id
                            + " cannot be deleted because borrowing history exists"
            );
        }
        memberRepository.deleteById(id);
    }

     private void  validateEmailForUpdate(
            String email,
            Integer id
    ) {
        String normalizedemail = email.trim().toLowerCase();
        if (memberRepository.existsByEmailIgnoreCaseAndIdNot(normalizedemail, id)) {
            throw new BadRequestException("Email " + normalizedemail + " already exists");
        }
    }
    public Member updateMemberById(Integer id, CreateMemberRequest request) {

        Member existingMember = memberRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member with id " + id + " was not found"
                        )
                );
        String normalizedEmail =
               request.getEmail()
                        .trim()
                        .toLowerCase();
validateEmailForUpdate(normalizedEmail,id);
        existingMember.setName(request.getName());
        existingMember.setEmail(normalizedEmail);

        return memberRepository.save(existingMember);
    }
}