package kr.apartribebackend.article.repository.together;

import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.SingleTogetherResponse;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomTogetherRepository {

    Page<TogetherResponse> findMultipleTogethersByCategory(String category, Pageable pageable);

    Optional<Together> findTogetherForApartId(String apartId, Long togetherId);

}
