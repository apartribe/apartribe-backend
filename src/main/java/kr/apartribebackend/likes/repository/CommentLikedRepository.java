package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.CommentLiked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikedRepository extends JpaRepository<CommentLiked, Long> {

}
