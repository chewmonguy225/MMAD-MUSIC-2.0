package com.MMAD.MMAD.user;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.MMAD.MMAD.exception.UserNotFoundException;
import com.MMAD.MMAD.model.User.User;
import com.MMAD.MMAD.model.User.UserDTO;
import com.MMAD.MMAD.model.User.UserDTOMapper;
import com.MMAD.MMAD.repo.UserRepo;
import com.MMAD.MMAD.service.UserService;

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
        UserDTO expected = userDTOMapper.apply(user);

        when(userRepo.findUserById(id)).thenReturn(Optional.of(user));

        UserDTO actual = underTest.findUserById(id);

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void willThrowWhenUserNotFound() {
        Long id = 53L;

        when(userRepo.findUserById(id)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> underTest.findUserById(id));
        assertThat(exception).isNotNull();
    }


    // @Test
    // void canCreateUser() {
    //     User user = new User(1L, "user1", "password");
    //     UserDTO expected = userDTOMapper.apply(user);

    //     when(userRepo.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());
    //     when(userRepo.save(user)).thenReturn(user);

    //     UserDTO actual = underTest.createUser(user.getUsername(), user.getPassword());

    //     assertThat(actual).isEqualTo(expected);
    // }


    @Test
    void willThrowWhenUserAlreadyExists() {
        User user = new User(1L, "user1", "password");

        when(userRepo.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> underTest.createUser(user.getUsername(), user.getPassword()));
        assertThat(exception).isNotNull();
    }


    @Test
    void canDeleteUser() {
        Long id = 53L;
        User user = new User(id, "user1", "password");

        when(userRepo.findUserById(id)).thenReturn(Optional.of(user));

        underTest.deleteUser(id);

        verify(userRepo).delete(user);
    }
    

    @Test
    void willThrowWhenDeletingNonExistentUser() {
        Long id = 53L;

        when(userRepo.findUserById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> underTest.deleteUser(id));
        assertThat(exception).isNotNull();
    }
}
