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
    public ResponseEntity<String> getUser(@PathVariable Long id){
        try{
            User user = userService.getUser(id);
            return ResponseEntity.ok("Get user : " + user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false) String[] search,
            Pageable pageable) {
        try {

            PageResponse<List<UserResponse>> pageResponse = userService.getAllUsers(search,pageable);

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
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO request){
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
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
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















//    @GetMapping
//    public ApiResponse<?> getAllUser() {
//        log.info("Request to get all users");
//
//        List<User> users = userService.getAllUsers();
//        return ApiResponse.<List<User>>builder()
//                .code(HttpStatus.OK.value())
//                .data(users)
//                .message("Get all users successfully")
//                .build();
//    }
//
//    @PostMapping
//    public ApiResponse<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
//        log.info("Request to create user: {}", userRequestDTO);
//
//        User user = userService.createUser(userRequestDTO);
//        return ApiResponse.<User>builder()
//                .code(HttpStatus.CREATED.value())
//                .data(user)
//                .message("user created successfully")
//                .build();
//    }
//
//    @PutMapping
//    public ApiResponse<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO) {
//        // Todo
//        return ApiResponse.<UserResponseDTO>builder()
//                .build();
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<UserDTO> getById(@PathVariable Long id) {
//       //Todo
//        return ApiResponse.<UserDTO>builder()
//                .code(HttpStatus.OK.value())
//                .message("Get user by id successfully")
//                .data(null)
//                .build();
//    }
//
//    @DeleteMapping
//    public ApiResponse<?> deleteUser() {
//        return ApiResponse.<String>builder()
//                .code(HttpStatus.NO_CONTENT.value())
//                .message("Delete user successfully")
//                .build();
//    }
}
