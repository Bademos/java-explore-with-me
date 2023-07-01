package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.models.user.UserDto;

import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> getUsers();

    List<User> getUserByListOfId(List<Long> list, int from, int size);

    User getUserById(Long id);

    User delete(Long id);
}
