package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.comment.Comment;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {
    UserRepository userRepository;
    EventRepository eventRepository;
    CommentRepository commentRepository;

    @Override
    public Comment addComment(String text, Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("The user with id " + userId + " is not found")
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("The event with id " + eventId + " is not found")
        );

        Comment comment = Comment.builder()
                .commentator(user)
                .text(text)
                .event(event)
                .date(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long commentId, Long userId, String text) {
        Comment oldComment = getCommentById(commentId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("The user with id " + userId + " is not found")
        );
        if (!oldComment.getCommentator().getId().equals(userId)) {
            throw new ConflictException("The creaed is not created by the user");
        }

        oldComment.setText(text);

        return commentRepository.save(oldComment);
    }

    @Override
    public List<Comment> getAllByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("The user with id " + userId + " is not found")
        );
        PageRequest pg = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        return commentRepository.findAllByCommentatorId(userId, pg);
    }

    @Override
    public List<Comment> getAllByEvent(Long eventId, int from, int size) {
        PageRequest pg = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("The event with id " + eventId + " is not found")
        );

        return commentRepository.findAllByEventId(eventId, pg);
    }

    @Override
    public List<Comment> getAll(int from, int size) {
        Pageable pg = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        return commentRepository.findAll(pg).toList();
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("The comment is not found")
        );
    }

    @Override
    public void deleteComment(Long id) {
        getCommentById(id);
        commentRepository.deleteById(id);
    }
}