package kr.apartribebackend.member.service;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.likes.domain.Liked;
import kr.apartribebackend.likes.repository.LikedRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.exception.MalformedProfileImageLinkException;
import kr.apartribebackend.member.exception.UserCantUpdateNicknameException;
import kr.apartribebackend.member.exception.UserCantUpdatePasswordException;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberConfigRepository;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.member.repository.agreements.AgreementsRepository;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberConfigService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConfigRepository memberConfigRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikedRepository likedRepository;
    private final AgreementsRepository agreementsRepository;
    private final EmailTokenRepository emailTokenRepository;

    public void updateSingleMemberNickname(final AuthenticatedMember authenticatedMember, final String nickname) {
        if (memberRepository.existsByNickname(nickname))
            throw new UserCantUpdateNicknameException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.updateNickname(nickname);
    }

    public void updateSingleMemberPassword(final AuthenticatedMember authenticatedMember,
                                           final MemberChangePasswordReq memberChangePasswordReq) {
        if (!passwordEncoder.matches(memberChangePasswordReq.currentPassword(), authenticatedMember.getPassword()))
            throw new UserCantUpdatePasswordException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.changePassword(passwordEncoder.encode(memberChangePasswordReq.newPassword()));
    }

    // TODO MemberJoinController 에도 똑같은 것이 있으니, profileImageUrl 검증을 분리하도록 하자.
    public void updateSingleMemberProfileImage(final AuthenticatedMember authenticatedMember,
                                               final MemberChangeImageReq memberChangeImageReq) {
        if (memberChangeImageReq.profileImageUrl() != null) {
            if (
                    StringUtils.containsWhitespace(memberChangeImageReq.profileImageUrl()) ||
                            memberChangeImageReq.profileImageUrl().contains("..") ||
                            memberChangeImageReq.profileImageUrl().contains("\\")
            ) {
                throw new MalformedProfileImageLinkException();
            }
        }
        final Member member = authenticatedMember.getOriginalEntity();
        member.updateProfileImageUrl(memberChangeImageReq.profileImageUrl());
    }

    public void deleteSingleUser(final MemberDto memberDto) {
        List<Long> likedIdsForMember = likedRepository.findLikedsByMemberId(memberDto.getId()).stream().map(Liked::getId).toList();
        likedRepository.deleteLikedsUsingLikedIds(likedIdsForMember);

        List<Board> boardsForMember  = boardRepository.findBoardsByMemberId(memberDto.getId());
        List<Long> boardIdsForMember = boardsForMember.stream().map(Board::getId).toList();
        List<Long> childCommentsIdsInBoards = commentRepository.findChildCommentsInBoardIds(boardIdsForMember).stream().map(Comment::getId).toList();
        List<Long> parentCommentsIdsInBoards = commentRepository.findParentCommentsInBoardIds(boardIdsForMember).stream().map(Comment::getId).toList();
        commentRepository.deleteCommentsUsingCommentIds(childCommentsIdsInBoards);
        commentRepository.deleteCommentsUsingCommentIds(parentCommentsIdsInBoards);
        boardRepository.deleteBoardsUsingBoardIds(boardIdsForMember);

        List<Long> leftParentCommentIds = commentRepository.findParentCommentsByMemberId(memberDto.getId()).stream().map(Comment::getId).toList();
        List<Long> leftChildCommentIds = commentRepository.findChildCommentsByMemberId(memberDto.getId()).stream().map(Comment::getId).toList();
        commentRepository.deleteCommentsUsingCommentIds(leftChildCommentIds);
        commentRepository.deleteCommentsUsingCommentIds(leftParentCommentIds);

        agreementsRepository.deleteAgreementsByMemberId(memberDto.getId());
        emailTokenRepository.deleteEmailTokenByMemberId(memberDto.getId());

        memberRepository.delete(memberDto.toEntity());
    }

    @Transactional(readOnly = true)
    public Page<MemberCommentRes> fetchCommentsForMember(final MemberDto memberDto,
                                                         final ApartmentDto apartmentDto,
                                                         final Pageable pageable) {
        return memberConfigRepository.findCommentsForMember(memberDto.toEntity(), apartmentDto.toEntity(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<MemberBoardResponse> fetchArticlesForMember(final MemberDto memberDto,
                                                            final ApartmentDto apartmentDto,
                                                            final Pageable pageable) {
        return memberConfigRepository.findArticlesForMember(
                memberDto.toEntity(),
                apartmentDto.toEntity(),
                pageable
        );
    }

}

//        List<Comment> commentsInBoards = commentRepository.findCommentsInBoardIds(boardIdsForMember);
//        List<Long> parentCommentIdsForBoard = commentsInBoards.stream().filter(comment -> comment.getParent() == null).map(Comment::getId).toList();
//        List<Long> childCommentIdsForBoard = commentsInBoards.stream().filter(comment -> !parentCommentIdsForBoard.contains(comment)).map(Comment::getId).toList();
//        commentRepository.deleteCommentsUsingCommentIds(childCommentIdsForBoard);
//        commentRepository.deleteCommentsUsingCommentIds(parentCommentIdsForBoard);
//        boardRepository.deleteBoardsUsingBoardIds(boardIdsForMember);