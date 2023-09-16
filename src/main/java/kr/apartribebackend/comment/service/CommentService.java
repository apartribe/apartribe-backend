package kr.apartribebackend.comment.service;

import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.eception.CannotApplyCommentException;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    @Transactional
    public void appendCommentToArticle(final MemberDto memberDto,
                                       final Long articleId,
                                       final CommentDto commentDto) {
        articleRepository.findById(articleId)
                .ifPresentOrElse(article ->
                                commentRepository.save(
                                        commentDto.toEntity(memberDto.toEntity(), article))
                , () -> { throw new CannotApplyCommentException(); });
    }

    public List<BestCommentResponse> bestCommentRankViaLastWeek() {
        return commentRepository.bestCommentRankViaLastWeek();
    }
}
