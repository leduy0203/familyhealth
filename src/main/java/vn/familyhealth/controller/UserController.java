package vn.familyhealth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.familyhealth.mapper.UserMapper;
import vn.familyhealth.model.User;
import vn.familyhealth.model.dto.UserDTO;
import vn.familyhealth.model.dto.request.UserRequestDTO;
import vn.familyhealth.model.dto.response.ApiResponse;
import vn.familyhealth.model.dto.response.UserResponseDTO;
import vn.familyhealth.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    @GetMapping
    public ApiResponse<?> getAllUser() {

        List<User> users = userService.getAllUsers();

        return ApiResponse.<List<User>>builder()
                .code(HttpStatus.CREATED.value())
                .data(users)
                .message("Get all users successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        // Todo
        return ApiResponse.<UserResponseDTO>builder()
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

        UserDTO userDTO = UserMapper.userDTO(userService.getUserById(id));

        return ApiResponse.<UserDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get user by id successfully")
                .data(userDTO)
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
