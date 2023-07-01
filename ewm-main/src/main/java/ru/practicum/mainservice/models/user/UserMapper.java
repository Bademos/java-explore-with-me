package ru.practicum.mainservice.models.user;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public User makeUserFromDto(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public User makeUserFromNewUserRequest(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }


    public UserDto makeUserDtoFromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto makeUserShortDtoFromUser(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}