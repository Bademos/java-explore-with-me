package ru.practicum.mainservice.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.comment.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommentatorId(Long userId, PageRequest pg);

    List<Comment> findAllByEventId(Long userId, PageRequest pg);
}
