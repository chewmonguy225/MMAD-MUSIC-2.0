package com.MMAD.Service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.MMAD.Security.JWTService;
import com.MMAD.Service.user.EmailService;
import com.MMAD.dto.user.LoginResponse;
import com.MMAD.dto.user.UserDTO;
import com.MMAD.dto.user.UserDTOMapper;
import com.MMAD.exception.UserNotFoundException;
import com.MMAD.model.User.User;
import com.MMAD.repo.UserRepo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder;
        private final JWTService jwtService;
        private final UserDTOMapper userDTOMapper;
        private final EmailService emailService;

        public UserService(
                        UserRepo userRepo,
                        JWTService jwtService,
                        PasswordEncoder passwordEncoder,
                        UserDTOMapper userDTOMapper,
                        EmailService emailService) {

                if (userRepo == null) {
                        throw new RuntimeException("userRepo cannot be null");
                }

                if (passwordEncoder == null) {
                        throw new RuntimeException("passwordEncoder cannot be null");
                }

                if (emailService == null) {
                        throw new RuntimeException("emailService cannot be null");
                }

                this.userRepo = userRepo;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.userDTOMapper = userDTOMapper;
                this.emailService = emailService;
        }

        @GetMapping("/me")
        public UserDTO getCurrentUser() {

                String username = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                return getUserDTOByUsername(username);
        }

        @Transactional
        public UserDTO getUserDTOById(Long id) {

                return userRepo.findUserById(id)
                                .map(userDTOMapper::apply)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "user with id [%s] not found".formatted(id)));
        }

        @Transactional
        public UserDTO getUserDTOByUsername(String username) {

                return userRepo.findUserByUsername(username)
                                .map(userDTOMapper::apply)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "user with username [%s] not found".formatted(username)));
        }

        @Transactional
        public Optional<User> getUserByUsername(String username) {

                return userRepo.findUserByUsername(username);
        }

        public LoginResponse login(String username, String password) {

                User user = userRepo.findUserByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (!passwordEncoder.matches(password, user.getPassword())) {
                        throw new RuntimeException("Invalid credentials");
                }

                if (!user.isVerified()) {
                        throw new RuntimeException("Account not verified");
                }

                String token = jwtService.generateToken(user.getUsername());

                return new LoginResponse(
                                token,
                                user.getUsername());
        }

        public void createUser(
                        String username,
                        String email,
                        String password) {

                Optional<User> existingUsername = userRepo.findUserByUsername(username);

                if (existingUsername.isPresent()) {
                        throw new RuntimeException(
                                        "Username already exists");
                }

                Optional<User> existingEmail = userRepo.findUserByEmail(email);

                if (existingEmail.isPresent()) {
                        throw new RuntimeException(
                                        "Email already exists");
                }

                User newUser = new User(
                                username,
                                passwordEncoder.encode(password),
                                email);

                newUser.setVerified(false);

                String code = generateVerificationCode();

                newUser.setVerificationCode(code);

                userRepo.save(newUser);

                try {

                        emailService.sendVerificationEmail(
                                        email,
                                        code);

                } catch (Exception e) {

                        e.printStackTrace();

                        throw new RuntimeException(
                                        "Verification email failed: " + e.getMessage());

                }
        }

        public void verifyUser(
                        String email,
                        String code) {

                User user = userRepo.findUserByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (user.getVerificationCode() == null ||
                                !user.getVerificationCode().equals(code)) {

                        throw new RuntimeException(
                                        "Invalid verification code");
                }

                user.setVerified(true);
                user.setVerificationCode(null);

                userRepo.save(user);
        }

        public void deleteUser(Long id) {

                User user = userRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "User does not exist"));

                user.getFollowers().forEach(follower -> {
                        follower.getFollowing().remove(user);
                        userRepo.save(follower);
                });

                user.getFollowing().forEach(followingUser -> {
                        followingUser.getFollowers().remove(user);
                        userRepo.save(followingUser);
                });

                user.getFollowers().clear();
                user.getFollowing().clear();

                userRepo.delete(user);
        }

        public void followUser(
                        String username,
                        String toFollowUsername) {

                if (username.equals(toFollowUsername)) {
                        throw new IllegalArgumentException(
                                        "Cannot follow yourself");
                }

                User user = userRepo.findUserByUsername(username)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "User not found"));

                User toFollow = userRepo.findUserByUsername(toFollowUsername)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "User to follow not found"));

                if (user.getFollowing().contains(toFollow)) {
                        throw new IllegalArgumentException(
                                        "Already following this user");
                }

                user.getFollowing().add(toFollow);
                toFollow.getFollowers().add(user);

                userRepo.save(user);
                userRepo.save(toFollow);
        }

        public void unfollowUser(
                        String username,
                        String toUnfollowUsername) {

                User user = userRepo.findUserByUsername(username)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "User not found"));

                User toUnfollow = userRepo.findUserByUsername(toUnfollowUsername)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "User not found"));

                user.getFollowing().remove(toUnfollow);
                toUnfollow.getFollowers().remove(user);

                userRepo.save(user);
                userRepo.save(toUnfollow);
        }

        public List<String> getFollowingList(Long userId) {

                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "User not found"));

                return user.getFollowing()
                                .stream()
                                .map(User::getUsername)
                                .collect(Collectors.toList());
        }

        public List<UserDTO> getFollowers(Long userId) {

                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "User not found"));

                return user.getFollowers()
                                .stream()
                                .map(userDTOMapper::apply)
                                .collect(Collectors.toList());
        }

        public List<UserDTO> searchUsers(String query) {

                if (query == null || query.trim().isEmpty()) {
                        return List.of();
                }

                return userRepo.findByUsernameContainingIgnoreCase(query)
                                .stream()
                                .map(userDTOMapper::apply)
                                .collect(Collectors.toList());
        }

        private String generateVerificationCode() {

                return String.valueOf(
                                (int) (Math.random() * 900000) + 100000);
        }
}