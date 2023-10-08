package kr.apartribebackend.category.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.category.dto.ArticleCategoryAppendReq;
import kr.apartribebackend.category.dto.CategoryListRes;
import kr.apartribebackend.category.dto.TogetherCategoryAppendReq;
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
    public ResponseEntity<Void> addArticleCategory(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final ArticleCategoryAppendReq articleCategoryAppendReq
    ) {
        categoryService.addArticleCategory(ARTICLE, articleCategoryAppendReq.toDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/api/category/together/add")
    public ResponseEntity<Void> addTogetherCategory(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final TogetherCategoryAppendReq togetherCategoryAppendReq
    ) {
        categoryService.addTogetherCategory(TOGETHER, togetherCategoryAppendReq.toDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
