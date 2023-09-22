package kr.apartribebackend.global.domain;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString @Getter
@MappedSuperclass
public abstract class BaseEntity extends TimeBaseEntity {

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", nullable = false)
    private String updatedBy;

}
