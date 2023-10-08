package kr.apartribebackend.category.domain;

import jakarta.persistence.*;
import kr.apartribebackend.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter @SuperBuilder
@Entity @Table(name = "CATEGORY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TAG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "TAG", insertable = false, updatable = false)
    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

}


//package kr.apartribebackend.category.domain;
//
//import jakarta.persistence.*;
//import kr.apartribebackend.global.domain.BaseEntity;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.experimental.SuperBuilder;
//
//import java.util.Objects;
//
//@Getter @SuperBuilder
//@Entity @Table(name = "CATEGORY")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Category extends BaseEntity {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "CATEGORY_ID")
//    private Long id;
//
//    @Column(name = "NAME", nullable = false, length = 50)
//    private String name;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Category category)) return false;
//        return id != null && Objects.equals(id, category.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////
//
//}
