package familyhealth.controller;

import familyhealth.model.Member;
import familyhealth.model.dto.MemberDTO;
import familyhealth.service.impl.MemberService;
import familyhealth.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getMember(@PathVariable Long id){
        try{
            Member member = memberService.getMember(id);
            return ResponseEntity.ok("Get Member : " + member);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createMember(@RequestBody MemberDTO memberDTO){
        try {
            Member member = memberService.createMember(memberDTO);
            return ResponseEntity.ok("Created Member: " + member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id,
                                        @RequestBody MemberDTO memberDTO){
        try{
            Member member = memberService.updateMember(id, memberDTO);
            return ResponseEntity.ok("Update Member : " + member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id){
        try{
            memberService.deleteMember(id);
            return ResponseEntity.ok("Delete Member : " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
