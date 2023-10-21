package kr.apartribebackend.article.repository.together;

import kr.apartribebackend.article.domain.RecruitStatus;
import kr.apartribebackend.article.domain.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface TogetherRepository
        extends JpaRepository<Together, Long>, CustomTogetherRepository {

    @Modifying(clearAutomatically = true)
    @Query(value = "update Together as t set t.recruitStatus = :recruitStatus where t.recruitTo >= :currentDate")
    int updateRecruitStatusByRecruitTo(
            @Param("recruitStatus") RecruitStatus recruitStatus,
            @Param("currentDate") LocalDate currentDate
    );

}
