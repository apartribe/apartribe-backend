package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kr.apartribebackend.category.domain.CategoryTag.*;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AttachmentService attachmentService;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Article appendArticle(final String category,
                                 final ArticleDto articleDto,
                                 final MemberDto memberDto) {
        final Category categoryEntity = categoryRepository.findCategoryByTagAndName(ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member memberEntity = memberDto.toEntity();
        final Article articleEntity = articleDto.toEntity(categoryEntity, memberEntity);
        return articleRepository.save(articleEntity);
    }

    @Transactional
    public void appendArticle(final String category,
                              final ArticleDto articleDto,
                              final MemberDto memberDto,
                              final List<MultipartFile> file) throws IOException {
        final Article article = appendArticle(category, articleDto, memberDto);
        final List<Attachment> attachments = attachmentService.saveFiles(file);
        for (Attachment attachment : attachments) {
            attachment.registBoard(article);
        }
        attachmentService.saveAttachments(attachments);
    }

    @Transactional
    public SingleArticleResponse updateArticle(final Long articleId,
                                    final String category,
                                    final ArticleDto articleDto,
                                    final MemberDto memberDto) {
        final Article articleEntity = articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
        final Category categoryEntity = categoryRepository.findCategoryByTagAndName(ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Article updatedArticle = articleEntity
                .updateArticle(categoryEntity, articleDto.getTitle(), articleEntity.getContent());
        return SingleArticleResponse.from(updatedArticle);
    }

    @Transactional
    public void updateLikeByArticleId(final Long articleId) {
        articleRepository.findById(articleId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

    public Page<ArticleResponse> findMultipleArticlesByCategory(final String category,
                                                                final Pageable pageable) {
        return articleRepository.findArticlesByCategory(category, pageable);
    }


//    public void removeArticle(final Board board) {
//        final List<Comment> comments = commentRepository.findCommentsForBoard(board);
//        final List<Comment> children = comments.stream()
//                .filter(comment -> !comment.getChildren().isEmpty())
//                .flatMap(comment -> comment.getChildren().stream())
//                .toList();
//
//        commentRepository.deleteAllInBatch(children);
//        commentRepository.deleteAllInBatch(comments);
//        boardRepository.delete(board);
//    }

    @Transactional
    public SingleArticleResponse findSingleArticleById(final Long articleId) {
        return articleRepository.findJoinedArticleById(articleId)
                .stream().findFirst()
                .orElseThrow(ArticleNotFoundException::new);
    }

    public List<Top5ArticleResponse> findTop5ArticleViaLiked() {
        return articleRepository.findTop5ArticleViaLiked();
    }

    public List<Top5ArticleResponse> findTop5ArticleViaView() {
        return articleRepository.findTop5ArticleViaView();
    }

    public List<ArticleInCommunityRes> searchArticleInCommunity(String title) {
        return articleRepository.searchArticleInCommunity(title);
    }

}
