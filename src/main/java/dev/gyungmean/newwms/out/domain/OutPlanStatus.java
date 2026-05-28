package dev.gyungmean.newwms.out.domain;

public enum OutPlanStatus {
    WAITING,        // 00 - 출고 대기
    LOADING_WAIT,   // 03 - 상차 대기
    STUFFING_DONE,  // 04 - Stuffing 완료 (수출)
    LOAD_CONFIRMED  // 05 - 상차 확정
}
