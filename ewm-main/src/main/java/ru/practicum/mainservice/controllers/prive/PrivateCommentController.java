package ru.practicum.mainservice.controllers.prive;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.comment.CommentDto;
import ru.practicum.mainservice.models.comment.CommentMapper;
import ru.practicum.mainservice.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
@RequestMapping("/users/{userId}")
public class PrivateCommentController {
    CommentService commentService;

    @GetMapping("/comments")
    List<CommentDto> getAllByUser(@PathVariable @Positive Long userId,
                                  @RequestParam @Positive int from,
                                  @RequestParam @PositiveOrZero int size) {
        log.info("Got request for all comments by user with id:{}", userId);
        return commentService.getAllByUser(userId, from, size).stream()
                .map(CommentMapper::makeCommentDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto addComment(@RequestParam(defaultValue = "First comment") @Valid String text,
                          @PathVariable @Positive Long userId,
                          @PathVariable @Positive Long eventId) {
        log.info("Got request for adding new comment to event with id:{} by user with id:{}", userId, eventId);
        //return CommentMapper.makeCommentDto(commentService.addComment(text, userId, eventId));
        return null;
    }

    @PatchMapping("/comments/{commentId}")
    CommentDto updateComment(@RequestParam(defaultValue = "bojia rosa") String text,
                             @PathVariable @Positive Long userId,
                             @PathVariable @Positive Long commentId) {
        log.info("Got request for updating  comment with id:{} by user with id:{}", commentId, userId);
        return CommentMapper.makeCommentDto(commentService.updateComment(commentId, userId, text));
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commentId) {
        log.info("Got request to delete comment with id:{}", commentId);
        commentService.deleteComment(commentId);
    }
}