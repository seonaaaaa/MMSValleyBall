package com.team.MMSValleyBall.enums;

import lombok.Getter;

@Getter
public enum MailStatus {
    DEFAULT,  // 메일 발송 필요 없음 (기본)
    REQUIRED;  // 메일 발송 필요 (경기 정보 변경 시)
}
