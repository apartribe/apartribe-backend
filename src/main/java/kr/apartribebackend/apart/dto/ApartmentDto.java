package kr.apartribebackend.apart.dto;


import kr.apartribebackend.apart.domain.Apartment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApartmentDto {

    private Long id;
    private String code;
    private String name;

    @Builder
    public ApartmentDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static ApartmentDto from(Apartment apartment) {
        return ApartmentDto.builder()
                .code(apartment.getCode())
                .name(apartment.getName())
                .build();
    }

    public Apartment toEntity() {
        return Apartment.builder()
                .id(id)
                .code(code)
                .name(name)
                .build();
    }
}
