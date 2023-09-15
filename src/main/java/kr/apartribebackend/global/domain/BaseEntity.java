package kr.apartribebackend.global.domain;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

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
