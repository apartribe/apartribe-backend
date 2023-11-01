package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.dto.together.SingleArticleResponseProjection;
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

    /**
     * 커뮤니티 게시글 단일 조회 (1) - 쿼리를 나눠서 세번 실행
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
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

    /**
     * 커뮤니티 게시글 단일 조회 (2) - SubQuery 를 포함한 한방 쿼리
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    @Transactional
    public SingleArticleResponseProjection findSingleArticleById2(final MemberDto memberDto,
                                                                  final String apartId,
                                                                  final Long articleId) {
        return articleRepository.findArticleForApartId(memberDto.getId(), apartId, articleId)
                .orElseThrow(ArticleNotFoundException::new);
    }

    /**
     * 커뮤니티 게시글 전체 조회 + 페이징
     * @param apartId
     * @param category
     * @param pageable
     * @return
     */
    public Page<ArticleResponse> findMultipleArticlesByCategory(final String apartId,
                                                                final String category,
                                                                final Pageable pageable) {
        return articleRepository.findArticlesByCategory(apartId, category, pageable);
    }

    /**
     * 커뮤니티 게시글 생성
     * @param apartId
     * @param category
     * @param articleDto
     * @param memberDto
     * @return
     */
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

    /**
     * 커뮤니티 게시글 생성 + AWS 업로드
     * @param apartId
     * @param category
     * @param articleDto
     * @param memberDto
     * @param file
     * @throws IOException
     */
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

    /**
     * 커뮤니티 게시글 수정
     * @param apartId
     * @param articleId
     * @param category
     * @param articleDto
     * @param memberDto
     * @return
     */
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
                .updateArticle(categoryEntity, articleDto.getTitle(), articleDto.getContent(), articleDto.getThumbnail());
        return SingleArticleResponse.from(updatedArticle, updatedArticle.getMember());
    }

    /**
     * 커뮤니티 게시글 좋아요
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
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

    /**
     * 베스트 게시물 (좋아요 순) Widget
     * @param apartId
     * @return
     */
    public List<Top5ArticleResponse> findTop5ArticleViaLiked(final String apartId) {
        return articleRepository.findTop5ArticleViaLiked(apartId);
    }

    /**
     * 베스트 게시물 (조회수 순) Widget
     * @param apartId
     * @return
     */
    public List<Top5ArticleResponse> findTop5ArticleViaView(final String apartId) {
        return articleRepository.findTop5ArticleViaView(apartId);
    }

    /**
     * 커뮤니티 내 게시글 검색 Widget
     * @param apartId
     * @param title
     * @return
     */
    public List<ArticleInCommunityRes> searchArticleInCommunity(final String apartId, final String title) {
        return articleRepository.searchArticleInCommunity(apartId, title);
    }

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
