package kr.apartribebackend.article.repository.together;

import kr.apartribebackend.article.dto.together.TogetherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomTogetherRepository {

    Page<TogetherResponse> findMultipleTogethersByCategory(String category, Pageable pageable);

}
