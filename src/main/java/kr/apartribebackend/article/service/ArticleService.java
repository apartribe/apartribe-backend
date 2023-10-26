package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.likes.domain.BoardLiked;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.likes.service.LikeService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
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
    private final BoardRepository boardRepository;
    private final LikeService likeService;

    @Transactional
    public Article appendArticle(final String apartId,
                                 final String category,
                                 final ArticleDto articleDto,
                                 final MemberDto memberDto) {
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member memberEntity = memberDto.toEntity();
        final Article articleEntity = articleDto.toEntity(categoryEntity, memberEntity);
        return articleRepository.save(articleEntity);
    }

    @Transactional
    public void appendArticle(final String apartId,
                              final String category,
                              final ArticleDto articleDto,
                              final MemberDto memberDto,
                              final List<MultipartFile> file) throws IOException {
        final Article article = appendArticle(apartId, category, articleDto, memberDto);
        final List<Attachment> attachments = attachmentService.saveFiles(file);
        for (Attachment attachment : attachments) {
            attachment.registBoard(article);
        }
        attachmentService.saveAttachments(attachments);
    }

    @Transactional
    public SingleArticleResponse updateArticle(final String apartId,
                                               final Long articleId,
                                               final String category,
                                               final ArticleDto articleDto,
                                               final MemberDto memberDto) {
        final Article articleEntity = articleRepository.findArticleForApartId(apartId, articleId)
                .orElseThrow(ArticleNotFoundException::new);
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Article updatedArticle = articleEntity
                .updateArticle(categoryEntity, articleDto.getTitle(), articleDto.getContent());
        return SingleArticleResponse.from(updatedArticle, updatedArticle.getMember());
    }

    @Transactional
    public BoardLikedRes updateLikeByArticleId(final MemberDto memberDto, final String apartId, final Long articleId) {
        final Board article = boardRepository.findBoardForApartId(apartId, articleId)
                .orElseThrow(CannotReflectLikeToArticleException::new);

        final BoardLiked boardLiked = likeService.findBoardLikedByMember(memberDto.getId(), article.getId())
                .orElse(null);
        if (boardLiked != null) {
            return likeService.decreaseLikesToBoard(boardLiked, article);
        }
        return likeService.increaseLikesToBoard(memberDto.toEntity(), article);
    }

    public Page<ArticleResponse> findMultipleArticlesByCategory(final String apartId,
                                                                final String category,
                                                                final Pageable pageable) {
        return articleRepository.findArticlesByCategory(apartId, category, pageable);
    }

    @Transactional
    public SingleArticleWithLikedResponse findSingleArticleById(final MemberDto memberDto,
                                                                final String apartId,
                                                                final Long articleId) {
        final SingleArticleResponse singleArticleResponse = articleRepository.findArticleForApartId(apartId, articleId)
                .map(article -> SingleArticleResponse.from(article, article.getMember()))
                .orElseThrow(ArticleNotFoundException::new);

        final BoardLikedRes memberLikedToBoard = likeService.isMemberLikedToBoard(memberDto.getId(), articleId);
        return SingleArticleWithLikedResponse.from(singleArticleResponse, memberLikedToBoard);
    }

    public List<Top5ArticleResponse> findTop5ArticleViaLiked(final String apartId) {
        return articleRepository.findTop5ArticleViaLiked(apartId);
    }

    public List<Top5ArticleResponse> findTop5ArticleViaView(final String apartId) {
        return articleRepository.findTop5ArticleViaView(apartId);
    }

    public List<ArticleInCommunityRes> searchArticleInCommunity(final String apartId, final String title) {
        return articleRepository.searchArticleInCommunity(apartId, title);
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

}
