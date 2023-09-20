package kr.apartribebackend.attachment.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class AttachmentService {

    private final AmazonS3 amazonS3Bucket;

    private final AttachmentRepository attachmentRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private Attachment saveFile(final MultipartFile multipartFile) throws IOException {
        final ObjectMetadata metadata = new ObjectMetadata();

        final String originalFilename = multipartFile.getOriginalFilename();
        final String extractedExt = extractedExt(originalFilename);
        final String contentType = multipartFile.getContentType();
        final long size = multipartFile.getSize();

        metadata.setContentLength(size);
        metadata.setContentType(contentType);

        amazonS3Bucket.putObject(bucketName, originalFilename, multipartFile.getInputStream(), metadata);
        final String uploadedUrl = amazonS3Bucket.getUrl(bucketName, originalFilename).toString();

        return createAttachment(originalFilename, extractedExt, contentType, uploadedUrl);
    }

    public List<Attachment> saveFiles(final List<MultipartFile> files) throws IOException {
        final ArrayList<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                attachments.add(saveFile(file));
            }
        }
        return attachments;
    }

    private String extractedExt(final String originalFilename) {
        int lastCommaIndex = originalFilename.lastIndexOf(".");
        return originalFilename.substring(lastCommaIndex + 1);
    }

    private static Attachment createAttachment(String originalFilename,
                                         String extractedExt,
                                         String contentType,
                                         String uploadedUrl) {
        return Attachment.builder()
                .fileName(originalFilename)
                .contentType(contentType)
                .extension(extractedExt)
                .uploadPath(uploadedUrl)
                .build();
    }

    @Transactional
    public void saveAttachments(List<Attachment> attachments) {
        attachmentRepository.saveAll(attachments);
    }


}
