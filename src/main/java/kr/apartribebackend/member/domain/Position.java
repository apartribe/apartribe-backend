package kr.apartribebackend.member.domain;

import lombok.Getter;

public enum Position {

    OWNER("자가"),
    LESSOR("임대인"),
    LESSEE("임차인"),
    SUBSCRIPTION_RIGHTS_HOLDER("청약권자"),
    ADMINISTRATOR("관리인"),
    SECURITY_GUARD("경비원"),
    APARTMENT_REPRESENTATIVE("아파트 대표"),
    BUILDING_REPRESENTATIVE("동 대표");

    @Getter private final String name;

    Position(String name) {
        this.name = name;
    }
}
