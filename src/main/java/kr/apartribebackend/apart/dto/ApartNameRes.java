package kr.apartribebackend.apart.dto;

public record ApartNameRes(
        String apartName
) {
    public static ApartNameRes from(ApartmentDto apartmentDto) {
        return new ApartNameRes(apartmentDto.getName());
    }
}
