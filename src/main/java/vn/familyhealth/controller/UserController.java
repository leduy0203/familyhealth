package vn.familyhealth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.familyhealth.model.User;
import vn.familyhealth.model.dto.UserDTO;
import vn.familyhealth.model.dto.request.UserRequestDTO;
import vn.familyhealth.model.dto.response.ApiResponse;
import vn.familyhealth.model.dto.response.UserResponseDTO;
import vn.familyhealth.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    @GetMapping
    public ApiResponse<?> getAllUser() {
        log.info("Request to get all users");

        List<User> users = userService.getAllUsers();
        return ApiResponse.<List<User>>builder()
                .code(HttpStatus.OK.value())
                .data(users)
                .message("Get all users successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Request to create user: {}", userRequestDTO);

        User user = userService.createUser(userRequestDTO);
        return ApiResponse.<User>builder()
                .code(HttpStatus.CREATED.value())
                .data(user)
                .message("user created successfully")
                .build();
    }

    @PutMapping
    public ApiResponse<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        // Todo
        return ApiResponse.<UserResponseDTO>builder()
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getById(@PathVariable Long id) {
       //Todo
        return ApiResponse.<UserDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get user by id successfully")
                .data(null)
                .build();
    }

    @DeleteMapping
    public ApiResponse<?> deleteUser() {
        return ApiResponse.<String>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete user successfully")
                .build();
    }
}
