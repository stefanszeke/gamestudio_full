package sk.tuke.gamestudio_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.entity.CommentRequest;
import sk.tuke.gamestudio_backend.entity.user.User;
import sk.tuke.gamestudio_backend.entity.user.UserLoginRequest;
import sk.tuke.gamestudio_backend.entity.user.UserRegisterRequest;
import sk.tuke.gamestudio_backend.entity.user.UserResponse;
import sk.tuke.gamestudio_backend.service.UserService;
import sk.tuke.gamestudio_backend.service.exceptions.UserException;

import java.sql.Timestamp;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // pages
    @GetMapping("pages/login")
    public ModelAndView toLogin() {
        return new ModelAndView("pages/login");
    }
    @GetMapping("pages/register")
    public ModelAndView  toRegister() {
        return new ModelAndView("pages/register");
    }


    // api
    @PostMapping("api/user/register")
    public RedirectView addComment(@ModelAttribute UserRegisterRequest userRegisterRequest, RedirectAttributes redirectAttributes) {
        try {
            UserResponse user = userService.createUser(userRegisterRequest);
            System.out.println(user.getEmail().length());
            redirectAttributes.addFlashAttribute("user", user);
            return new RedirectView("/pages/login");
        } catch (UserException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return new RedirectView("/pages/register");
        }
    }

    @PostMapping("api/user/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) throws UserException {
        UserResponse user = userService.login(userLoginRequest);
        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException e) {
        return new ResponseEntity<>(Map.of("message",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
