package com.team.MMSValleyBall.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    // 사용자가 활성화되어 정상적으로 서비스를 이용할 수 있는 상태
    ACTIVE("사용자 활성 상태"),

    // 사용자가 비활성화되어 서비스 접근이 제한된 상태 (계정 비활성화 등)
    INACTIVE("사용자 비활성 상태");

    private final String description;

    // 상태에 대한 설명을 제공하는 생성자
    UserStatus(String description) {
        this.description = description;
    }

    // 상태의 설명을 반환하는 메서드
    public String getDescription() {
        return description;
    }
}
