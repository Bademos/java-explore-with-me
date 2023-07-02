package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public User addUser(User user) {
        checkNameUser(user);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserByListOfId(List<Long> list, int from, int size) {
        log.info("gettin usersby list id{}, from:{}, with size{}", list, from, size);
        from /= size;
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (list == null) {
            return userRepository.findAll(pr).toList();
        }
        if (list.size() == 0) return Collections.emptyList();
        return userRepository.findByIdIn(list, pr);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("There is no user with id:" + id));
    }

    @Override
    public User delete(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }

    private void checkNameUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new ConflictException("This name is already taken");
        }
    }
}