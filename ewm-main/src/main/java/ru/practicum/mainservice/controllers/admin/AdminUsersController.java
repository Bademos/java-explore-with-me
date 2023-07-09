package ru.practicum.mainservice.controllers.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.user.NewUserRequest;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.models.user.UserDto;
import ru.practicum.mainservice.models.user.UserMapper;
import ru.practicum.mainservice.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/users")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class AdminUsersController {
    UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "ids", required = false) List<Long> listId,
                                     @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Got request for all users");
        return userService.getUserByListOfId(listId, from, size).stream()
                .map(UserMapper::makeUserDtoFromUser)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest userDto) {
        log.info("get new UserDto: {}", userDto);
        User user = UserMapper.makeUserFromNewUserRequest(userDto);
        return UserMapper.makeUserDtoFromUser(userService.addUser(user));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return UserMapper.makeUserDtoFromUser(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto deleteUser(@PathVariable @Positive Long id) {
        log.info("Got request to delete user with id {}", id);
        return UserMapper.makeUserDtoFromUser(userService.delete(id));
    }
}