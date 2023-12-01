package kr.apartribebackend.attachment.controller;

import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/api/{apartId}/attach")
    public APIResponse<List<String>> attachmentToAWS(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @RequestParam final List<MultipartFile> file) throws IOException
    {
        final List<String> uploadPaths = attachmentService.saveFiles(file).stream().map(Attachment::getUploadPath).toList();
        final APIResponse<List<String>> apiResponse = APIResponse.SUCCESS(uploadPaths);
        return apiResponse;
    }
}
