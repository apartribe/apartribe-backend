package kr.apartribebackend.comment.dto;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {

    private Long id;
    private String content;
    private int liked;
    private Board board;

    @Builder
    private CommentDto(Long id,
                       String content,
                       int liked,
                       Board board) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.board = board;
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .liked(comment.getLiked())
                .board(comment.getBoard())
                .build();
    }

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .id(id)
                .content(content)
                .liked(liked)
                .board(board)
                .member(member)
                .build();
    }

}


//package kr.apartribebackend.comment.dto;
//
//import kr.apartribebackend.article.domain.Article;
//import kr.apartribebackend.comment.domain.Comment;
//import kr.apartribebackend.member.domain.Member;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class CommentDto {
//
//    private Long id;
//    private String content;
//    private int liked;
//    private Article article;
//
//    @Builder
//    private CommentDto(Long id,
//                       String content,
//                       int liked,
//                       Article article) {
//        this.id = id;
//        this.content = content;
//        this.liked = liked;
//        this.article = article;
//    }
//
//    public static CommentDto from(Comment comment) {
//        return CommentDto.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .liked(comment.getLiked())
//                .article(comment.getArticle())
//                .build();
//    }
//
//    public Comment toEntity(Member member, Article article) {
//        return Comment.builder()
//                .id(id)
//                .content(content)
//                .liked(liked)
//                .article(article)
//                .member(member)
//                .build();
//
//    }
//
//}
