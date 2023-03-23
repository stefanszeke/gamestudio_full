package sk.tuke.gamestudio_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio_backend.entity.user.User;
import sk.tuke.gamestudio_backend.entity.user.UserLoginRequest;
import sk.tuke.gamestudio_backend.entity.user.UserRegisterRequest;
import sk.tuke.gamestudio_backend.entity.user.UserResponse;
import sk.tuke.gamestudio_backend.service.UserService;
import sk.tuke.gamestudio_backend.service.exceptions.UserException;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) throws UserException {
        UserResponse user = userService.createUser(userRegisterRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) throws UserException {
        UserResponse user = userService.login(userLoginRequest);
        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException e) {
        return new ResponseEntity<>(Map.of("message",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
