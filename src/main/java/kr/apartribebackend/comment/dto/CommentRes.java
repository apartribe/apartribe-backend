package kr.apartribebackend.comment.dto;

import kr.apartribebackend.article.dto.SingleCommentResponse;
import kr.apartribebackend.comment.domain.Comment;
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
    private LocalDateTime createdAt;
    private String  createdBy;
    private List<CommentRes> children = new ArrayList<>();

    public CommentRes(Long id, String content, Integer liked, LocalDateTime createdAt, String createdBy) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static CommentRes from(Comment comment) {
        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getLiked(),
                comment.getCreatedAt(),
                comment.getCreatedBy()
        );
    }
}


//package kr.apartribebackend.comment.dto;
//
//import kr.apartribebackend.article.dto.SingleCommentResponse;
//import kr.apartribebackend.comment.domain.Comment;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public record CommentRes(
//        Long id,
//        String content,
//        Integer liked,
//        LocalDateTime createdAt,
//        String createdBy
////        List<CommentRes> children
//) {
//
//    public static CommentRes from(Comment comment) {
//        return new CommentRes(
//                comment.getId(),
//                comment.getContent(),
//                comment.getLiked(),
//                comment.getCreatedAt(),
//                comment.getCreatedBy()
////                comment.getChildren()
////                        .stream()
////                        .map(CommentRes::from)
////                        .collect(Collectors.toList())
//        );
//    }
//
//}
