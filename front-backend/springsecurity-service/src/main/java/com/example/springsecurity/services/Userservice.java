package com.example.springsecurity.services;




import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.User;

import java.util.List;
import java.util.Set;

public interface Userservice {


    User createUser(User User);
    User updateUser(User User);
    List<User> getAllUser();
    User getUserById(Long id);
    void deleteUser(Long id);
    User assignRoleChef(Long userId);
    User updateUserRoles(Long userId, Set<ERole> newRoles);

}
