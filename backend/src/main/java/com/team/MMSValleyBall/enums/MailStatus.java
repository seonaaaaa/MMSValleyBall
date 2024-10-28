package com.team.MMSValleyBall.enums;

import lombok.Getter;

@Getter
public enum MailStatus {
    // 메일 발송 필요 없음 (기본 상태)
    DEFAULT,
    // 메일 발송 필요 (경기 정보 수정, 삭제 시 변경됨)
    REQUIRED;
}
