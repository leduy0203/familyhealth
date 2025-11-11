package familyhealth.controller;

import familyhealth.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;


//@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id){
        try{
            User user = userService.getUser(id);
            return ResponseEntity.ok("Get user : " + user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> creatUser(@RequestBody UserDTO userDTO){
        try {
            User userNew = userService.createUser(userDTO);
            return ResponseEntity.ok("Created User: " + userNew);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//
//    @PostMapping("/register/member")
//    public ResponseEntity<?> creatUserMember(@RequestBody MemberRegisterDTO memberRegisterDTO){
//        try {
//            User createdUser = userService.createUserMember(memberRegisterDTO.getUserDTO(), memberRegisterDTO.getMemberDTO(), memberRegisterDTO.getHouseholdId());
//            return ResponseEntity.ok("Created User Member: " + createdUser.getFullName());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable Long id,
//                                             @RequestBody UserDTO userDTO){
//        try{
//            User user = userService.updateUser(id, userDTO);
//            return ResponseEntity.ok("Update user : " + user);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long id){
//        try {
//            userService.deleteUser(id);
//            return ResponseEntity.ok("Deleted User : " + id);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }













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
