package kr.apartribebackend.article.repository.together;

import kr.apartribebackend.article.dto.together.SingleTogetherResponse;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTogetherRepository {

    Page<TogetherResponse> findMultipleTogethersByCategory(String category, Pageable pageable);

    List<SingleTogetherResponse> findJoinedTogetherById(Long togetherId);

}
