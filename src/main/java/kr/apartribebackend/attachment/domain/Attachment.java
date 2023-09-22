package kr.apartribebackend.attachment.domain;

import jakarta.persistence.*;
import kr.apartribebackend.article.domain.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ATTACHMENT")
public class Attachment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID")
    public Long id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Column(name = "UPLOAD_PATH")
    private String uploadPath;

    @Column(name = "EXTENSION")
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public Attachment(Long id,
                      String fileName,
                      String contentType,
                      String uploadPath,
                      String extension,
                      Board board) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.uploadPath = uploadPath;
        this.extension = extension;
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ARTICLE_ID")
//    private Article article;

//    @Builder
//    public Attachment(Long id,
//                      String fileName,
//                      String contentType,
//                      String uploadPath,
//                      String extension,
//                      Article article) {
//        this.id = id;
//        this.fileName = fileName;
//        this.contentType = contentType;
//        this.uploadPath = uploadPath;
//        this.extension = extension;
//        this.article = article;
//    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public void registBoard(Board board) {
        if (this.board != null)
            this.board.getAttachments().remove(this);
        this.board = board;
        board.getAttachments().add(this);
    }

//    public void registArticle(Article article) {
//        this.article = article;
//    }

//    public void registBoard(Board board) {
//        this.board = board;
//    }

}
