package sk.tuke.gamestudio_backend.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio_backend.entity.user.User;
import sk.tuke.gamestudio_backend.entity.user.UserLoginRequest;
import sk.tuke.gamestudio_backend.entity.user.UserRegisterRequest;
import sk.tuke.gamestudio_backend.entity.user.UserResponse;
import sk.tuke.gamestudio_backend.repository.UserRepository;
import sk.tuke.gamestudio_backend.service.exceptions.UserException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse createUser(UserRegisterRequest userRegisterRequest) throws UserException {

        if(userRepository.findByUsername(userRegisterRequest.getUsername()) != null) {
            throw new UserException("Username already exists");
        }

        if(userRepository.findByEmail(userRegisterRequest.getEmail()) != null) {
            throw new UserException("Email already exists");
        }

        if(!userRegisterRequest.getPassword().equals(userRegisterRequest.getPassword2())) {
            throw new UserException("Passwords do not match");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(userRegisterRequest.getPassword());

        User user = new User(userRegisterRequest.getUsername(), hashedPassword, userRegisterRequest.getEmail());

        userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getEmail());
    }

    public UserResponse login(UserLoginRequest userLoginRequest) throws UserException {
        User user = userRepository.findByUsername(userLoginRequest.getUsername());
        if(user == null) {
            throw new UserException("Invalid credentials");
        }
        if(!bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new UserException("Invalid credentials");
        }
        return new UserResponse(user.getUsername(), user.getEmail());
    }

}
