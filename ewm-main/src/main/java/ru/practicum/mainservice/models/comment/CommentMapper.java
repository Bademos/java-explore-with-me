package ru.practicum.mainservice.models.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.user.UserMapper;

@UtilityClass
public class CommentMapper {
    public CommentDto makeCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .commentator(UserMapper.makeUserDtoFromUser(comment.getCommentator()))
                .date(comment.getDate())
                .text(comment.getText())
                .event(EventMapper.makeShortDto(comment.getEvent()))
                .build();
    }
}
