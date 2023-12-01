package kr.apartribebackend.comment.dto;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private Long id;
    private String content;
    private int liked;
    private Board board;
    private MemberDto memberDto;
    private String createdBy;
    private LocalDateTime createdAt;

    @Builder
    private CommentDto(Long id,
                       String content,
                       int liked,
                       Board board,
                       MemberDto memberDto,
                       String createdBy,
                       LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.board = board;
        this.memberDto = memberDto;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .liked(comment.getLiked())
                .board(comment.getBoard())
                .createdBy(comment.getCreatedBy())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentDto from(Comment comment, Member member) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .liked(comment.getLiked())
                .board(comment.getBoard())
                .memberDto(MemberDto.from(member))
                .createdBy(comment.getCreatedBy())
                .createdAt(comment.getCreatedAt())
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
