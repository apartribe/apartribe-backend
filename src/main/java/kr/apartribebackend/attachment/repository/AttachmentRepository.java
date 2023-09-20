package kr.apartribebackend.attachment.repository;

import kr.apartribebackend.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
