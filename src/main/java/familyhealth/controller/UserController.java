package familyhealth.controller;

import familyhealth.Utils.MessageKey;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.model.dto.response.UserResponse;
import familyhealth.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


//@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok("Get user : " + user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false) String[] search,
            Pageable pageable) {
        try {

            PageResponse<List<UserResponse>> pageResponse = userService.getAllUsers(search, pageable);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_LIST_USER_SUCCESS)
                    .data(pageResponse)
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO request) {
        try {

            User userNew = userService.createUser(request);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(CREATED.value())
                    .message(MessageKey.CREATE_USER_SUCCESS)
                    .data(userNew.getId())
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {

            this.userService.deleteUser(id);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(NO_CONTENT.value())
                    .message(MessageKey.CHANGE_STATUS_USER_SUCCESS)
                    .data(null)
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}