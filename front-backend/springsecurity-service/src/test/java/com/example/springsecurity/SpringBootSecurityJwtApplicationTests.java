package com.example.springsecurity;


import com.example.springsecurity.models.User;
import com.example.springsecurity.repository.UserRepository;

import com.example.springsecurity.services.Imp.UserserviceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SpringBootSecurityJwtApplicationTests {

    @InjectMocks
    private UserserviceImp userservice;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userservice.createUser(user);

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAllUser() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");

        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userservice.getAllUser();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userservice.getUserById(1L);

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("updateduser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userservice.updateUser(user);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getUsername(), updatedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userservice.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
