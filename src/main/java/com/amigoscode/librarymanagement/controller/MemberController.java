package com.amigoscode.librarymanagement.controller;
import com.amigoscode.librarymanagement.dto.CreateMemberRequest;
import com.amigoscode.librarymanagement.entity.Member;
import com.amigoscode.librarymanagement.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getMembers() {
        return memberService.getAllMembers();
    }

    @PostMapping
    public Member addMember(@Valid @RequestBody CreateMemberRequest member) {
        return memberService.addMember(member);
    }

    @GetMapping("{id}")
    public Member findMember(@PathVariable Integer id) {
        return memberService.findMemberById(id);
    }

    @DeleteMapping("{id}")
    public void deleteMember(@PathVariable Integer id) {
        memberService.deleteMemberById(id);
    }

    @PutMapping("{id}")
    public Member updateMember(
            @PathVariable Integer id,
           @Valid @RequestBody CreateMemberRequest updatedMember
    ) {
        return memberService.updateMemberById(id, updatedMember);
    }
}