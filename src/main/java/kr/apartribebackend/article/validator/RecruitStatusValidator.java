package kr.apartribebackend.article.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.article.annotation.IsRecruitStatusValid;
import kr.apartribebackend.article.domain.RecruitStatus;

import java.util.Arrays;



public class RecruitStatusValidator implements ConstraintValidator<IsRecruitStatusValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(RecruitStatus.values())
                .anyMatch(recruitStatus -> recruitStatus.getName().equals(value));
    }
}
