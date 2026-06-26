package com.MMAD.MMAD.user;

import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.MMAD.MMAD.exception.UserNotFoundException;
import com.MMAD.MMAD.model.User.User;
import com.MMAD.MMAD.model.User.UserDTO;
import com.MMAD.MMAD.model.User.UserDTOMapper;
import com.MMAD.MMAD.repo.UserRepo;
import com.MMAD.MMAD.service.UserService;

@Disabled
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    private final UserDTOMapper userDTOMapper = new UserDTOMapper();

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepo, userDTOMapper);
    }

    @Test
    void canGetUserById() {
        Long id = 53L;
        User user = new User(id, "user1", "password");

        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        UserDTO actual = underTest.findUserById(id);

        assertThat(actual).isEqualTo(userDTOMapper.apply(user));
    }

    @Test
    void willThrowWhenUserNotFound() {
        Long id = 53L;

        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> underTest.findUserById(id));
    }

    @Test
    void willThrowWhenUserAlreadyExists() {
        User user = new User(1L, "user1", "password");

        when(userRepo.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> underTest.createUser(user.getUsername(), user.getPassword()));
    }

    @Test
    void canDeleteUser() {
        Long id = 53L;

        User user = new User(id, "user1", "password");

        User follower = new User(2L, "follower", "pass");
        User following = new User(3L, "following", "pass");

        user.getFollowers().add(follower);
        user.getFollowing().add(following);

        follower.getFollowing().add(user);
        following.getFollowers().add(user);

        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        underTest.deleteUser(id);

        verify(userRepo).save(follower);
        verify(userRepo).save(following);
        verify(userRepo).delete(user);

        verifyNoMoreInteractions(userRepo);
    }

    @Test
    void willThrowWhenDeletingNonExistentUser() {
        Long id = 53L;

        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> underTest.deleteUser(id));
    }
}