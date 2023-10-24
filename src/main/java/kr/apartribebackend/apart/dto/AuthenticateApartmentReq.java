package kr.apartribebackend.apart.dto;

public record AuthenticateApartmentReq(
        String code,
        String name
) {
    public ApartmentDto toDto() {
        return ApartmentDto.builder()
                .code(code)
                .name(name)
                .build();
    }
}
