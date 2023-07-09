package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.comment.Comment;

import java.util.List;

public interface CommentService {
    public Comment addComment(String text, Long userId, Long eventId);

    public Comment updateComment(Long commentId, Long userId, String text);

    public List<Comment> getAllByUser(Long userId, int from, int size);

    public List<Comment> getAllByEvent(Long eventId, int from, int size);

    public List<Comment> getAll(int from, int size);

    public Comment getCommentById(Long commentId);

    void deleteComment(Long id);
}
