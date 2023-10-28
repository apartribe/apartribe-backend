package kr.apartribebackend.advertise.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.advertise.annotation.ValidateNonAuthCode;
import kr.apartribebackend.advertise.dto.AppendAdvertiseReq;

import static org.springframework.util.StringUtils.*;

public class NonAuthCodeValidator implements ConstraintValidator<ValidateNonAuthCode, AppendAdvertiseReq> {

    @Override
    public boolean isValid(AppendAdvertiseReq request, ConstraintValidatorContext context) {
        if (request.nonAuth()) {
            if (hasText(request.code())) {
                return true;
            }
        }
        if (!request.nonAuth()) {
            if (request.code() == null) {
                return true;
            }
        }
        return false;
    }
}
