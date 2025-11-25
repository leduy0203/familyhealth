package familyhealth.controller;

import familyhealth.Utils.MessageKey;
import familyhealth.model.Member;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.service.impl.MemberService;
import familyhealth.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final UserService userService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMember(@PathVariable Long id){
        try{

            Member member = memberService.getMember(id);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_MEMBER_SUCCESS)
                    .data(member)
                    .build()
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT_HOUSEHOLD')")
    public ResponseEntity<?> createMember(@Valid @RequestBody MemberRegisterDTO request){
        try {

            Member member = memberService.createMember(request);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.CREATE_MEMBER_SUCCESS)
                    .data(member.getId())
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id,
                                        @RequestBody MemberDTO memberDTO){
        try{
            Member member = memberService.updateMember(id, memberDTO);
            return ResponseEntity.ok("Update Member : " + member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/update/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id){
        try{

            memberService.deleteMember(id);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(NO_CONTENT.value())
                    .message(MessageKey.DELETED_MEMBERS_SUCCESS)
                    .data(null)
                    .build()
            );
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/families")
    public ResponseEntity<?> getMyFamilyMembers(@RequestParam(required = false) String[] search,
                                                Pageable pageable) {
        try {

            PageResponse<List<Member>> members = memberService.getFamilyMembers(search,pageable);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_ALL_MEMBERS_SUCCESS)
                    .data(members)
                    .build()
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
