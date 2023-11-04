package kr.apartribebackend.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApartUser {

    /**
     * 해당 옵션을 true 로 설정하면 아파트인증이 완료된 사용자임을 판단함과 동시에 PathVariable 정보와 인증객체의 apartCode 를 비교를 비교하여 HandlerMethod 에 접근 여부를 판단한다.
     * 해당 옵션을 false 로 설정하면 아파트인증이 완료된 사용자인지만 판단한다.
     * @return
     */
    boolean checkApartment() default true;

}
