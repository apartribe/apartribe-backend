package kr.apartribebackend.category.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.category.dto.CategoryAppendReq;
import kr.apartribebackend.category.service.CategoryService;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/category/add")
    public ResponseEntity<Void> addCategory(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final CategoryAppendReq categoryAppendReq
    ) {
        categoryService.addCategory(categoryAppendReq.toDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
