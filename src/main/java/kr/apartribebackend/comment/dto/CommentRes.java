package kr.apartribebackend.comment.dto;

import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.likes.dto.CommentLikedWithCommentAndMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRes{

    private Long id;
    private String content;
    private Integer liked;
    private boolean memberLiked;
    private LocalDateTime createdAt;
    private String  createdBy;
    private List<CommentRes> children = new ArrayList<>();

    public CommentRes(Long id,
                      String content,
                      Integer liked,
                      boolean memberLiked,
                      LocalDateTime createdAt,
                      String createdBy) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.memberLiked = memberLiked;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static CommentRes from(Comment comment, List<CommentLikedWithCommentAndMember> commentLikedWithCommentAndMember) {
        for (int i = 0; i < commentLikedWithCommentAndMember.size(); i++) {
            if (comment.getId().equals(commentLikedWithCommentAndMember.get(i).getCommentId())) {
                return new CommentRes(
                        comment.getId(),
                        comment.getContent(),
                        comment.getLiked(),
                        true,
                        comment.getCreatedAt(),
                        comment.getCreatedBy()
                );
            }
        }
        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getLiked(),
                false,
                comment.getCreatedAt(),
                comment.getCreatedBy()
        );
    }
}
