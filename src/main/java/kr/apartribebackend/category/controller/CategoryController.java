package kr.apartribebackend.category.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.dto.*;
import kr.apartribebackend.category.service.CategoryService;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @ApartUser
    @PostMapping("/api/{apartId}/category/article/add")
    public ResponseEntity<APIResponse<CategoryResponse>> addArticleCategory(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final ArticleCategoryAppendReq articleCategoryAppendReq
    ) {
        final Category category = categoryService.addArticleCategory(
                apartId,
                authenticatedMember.toDto(),
                articleCategoryAppendReq.toDto()
        );
        final CategoryResponse categoryResponse = CategoryResponse.from(category);
        final APIResponse<CategoryResponse> apiResponse = APIResponse.SUCCESS(categoryResponse);
        return ResponseEntity.status(CREATED).body(apiResponse);
    }

    @ApartUser
    @PostMapping("/api/{apartId}/category/together/add")
    public ResponseEntity<APIResponse<CategoryResponse>> addTogetherCategory(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final TogetherCategoryAppendReq togetherCategoryAppendReq
    ) {
        final Category category = categoryService.addTogetherCategory(
                apartId,
                authenticatedMember.toDto(),
                togetherCategoryAppendReq.toDto()
        );
        final CategoryResponse categoryResponse = CategoryResponse.from(category);
        final APIResponse<CategoryResponse> apiResponse = APIResponse.SUCCESS(categoryResponse);
        return ResponseEntity.status(CREATED).body(apiResponse);
    }

    @GetMapping("/api/{apartId}/category/article/list")
    public APIResponse<List<CategoryListRes>> listArticleCategory(
            @PathVariable final String apartId
    ) {
        final List<CategoryListRes> categoryListRes = categoryService.listArticleCategory(apartId);
        final APIResponse<List<CategoryListRes>> apiResponse = APIResponse.SUCCESS(categoryListRes);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/category/together/list")
    public APIResponse<List<CategoryListRes>> listTogetherCategory(
            @PathVariable final String apartId
    ) {
        final List<CategoryListRes> categoryListRes = categoryService.listTogetherCategory(apartId);
        final APIResponse<List<CategoryListRes>> apiResponse = APIResponse.SUCCESS(categoryListRes);
        return apiResponse;
    }

}
