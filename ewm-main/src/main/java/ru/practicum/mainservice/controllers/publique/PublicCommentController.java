package ru.practicum.mainservice.controllers.publique;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.comment.CommentDto;
import ru.practicum.mainservice.models.comment.CommentMapper;
import ru.practicum.mainservice.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping()
@Validated
public class PublicCommentController {
    CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getAllCommentsByEvent(@PathVariable @Positive Long eventId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "20") @Positive int size) {
        return commentService.getAllByEvent(eventId, from, size).stream()
                .map(CommentMapper::makeCommentDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
