package kr.apartribebackend.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class CommentResProjection {

    private Long parentId;
    private Long commentId;
    private String content;
    private Integer liked;
    private String profileImage;
    private boolean memberLiked;
    private boolean memberCreated;
    private int childCounts;
    private LocalDateTime createdAt;
    private String createdBy;
    private List<CommentResProjection> children = new ArrayList<>();

    public CommentResProjection(Long parentId,
                                Long commentId,
                                String content,
                                Integer liked,
                                String profileImage,
                                boolean memberLiked,
                                boolean memberCreated,
                                int childCounts,
                                LocalDateTime createdAt,
                                String createdBy) {
        this.parentId = parentId;
        this.commentId = commentId;
        this.content = content;
        this.liked = liked;
        this.profileImage = profileImage;
        this.memberLiked = memberLiked;
        this.memberCreated = memberCreated;
        this.childCounts = childCounts;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

}
