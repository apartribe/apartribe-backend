package kr.apartribebackend.category.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.dto.*;
import kr.apartribebackend.category.service.CategoryService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.apartribebackend.category.domain.CategoryTag.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/category/article/add")
    public ResponseEntity<APIResponse<CategoryResponse>> addArticleCategory(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final ArticleCategoryAppendReq articleCategoryAppendReq
    ) {
        final Category category = categoryService.addArticleCategory(ARTICLE, articleCategoryAppendReq.toDto());
        final CategoryResponse categoryResponse = CategoryResponse.from(category);
        final APIResponse<CategoryResponse> apiResponse = APIResponse.SUCCESS(categoryResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/api/category/together/add")
    public ResponseEntity<APIResponse<CategoryResponse>> addTogetherCategory(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final TogetherCategoryAppendReq togetherCategoryAppendReq
    ) {
        final Category category = categoryService.addTogetherCategory(TOGETHER, togetherCategoryAppendReq.toDto());
        final CategoryResponse categoryResponse = CategoryResponse.from(category);
        final APIResponse<CategoryResponse> apiResponse = APIResponse.SUCCESS(categoryResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // TODO 나중에 카테고리 리스트를 출력할때, 아파트 정보를 매개변수로 주고, 필터링해야 한다.
    @GetMapping("/api/category/article/list")
    public APIResponse<List<CategoryListRes>> listArticleCategory() {
        final List<CategoryListRes> categoryListRes = categoryService.listArticleCategory()
                .stream().map(CategoryListRes::from)
                .collect(Collectors.toList());
        final APIResponse<List<CategoryListRes>> apiResponse = APIResponse.SUCCESS(categoryListRes);
        return apiResponse;
    }

    // TODO 나중에 카테고리 리스트를 출력할때, 아파트 정보를 매개변수로 주고, 필터링해야 한다.
    @GetMapping("/api/category/together/list")
    public APIResponse<List<CategoryListRes>> listTogetherCategory() {
        final List<CategoryListRes> categoryListRes = categoryService.listTogetherCategory()
                .stream().map(CategoryListRes::from)
                .collect(Collectors.toList());
        final APIResponse<List<CategoryListRes>> apiResponse = APIResponse.SUCCESS(categoryListRes);
        return apiResponse;
    }

}
