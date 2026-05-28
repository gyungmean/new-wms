package dev.gyungmean.newwms.out.domain;

public enum OutOrderStatus {
    READY,      // 출고 지시 완료, 실행 대기
    IN_PROGRESS,// 출고 진행중
    COMPLETED   // 출고 완료
}
