package dev.gyungmean.newwms.out.domain;

public enum IfStatus {
    PENDING,    // ERP에서 전송됨, 아직 WMS에서 처리 안 함
    PROCESSED,  // WMS가 읽어서 OutPlan 생성 완료
    ERROR       // 처리 중 오류 발생
}
