package dev.gyungmean.newwms.inventory.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import dev.gyungmean.newwms.common.exception.ErrorCode;
import dev.gyungmean.newwms.common.exception.WmsStateException;
import dev.gyungmean.newwms.inventory.domain.vo.ReservationStatus;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import dev.gyungmean.newwms.master.domain.BagType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "w_stk_i", uniqueConstraints = @UniqueConstraint(
    name = "uk_stock_business_key",
    columnNames = {"storage_id", "rack_no", "item_code", "lot_date", "bag_type", "load_type"}
))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    /** 현재 위치 창고 ID */
    @Column(name = "storage_id", length = 10, nullable = false)
    private String storageId;

    /** 현재 위치 랙 번호 (이동 시 업데이트) */
    @Column(name = "rack_no", length = 8, nullable = false)
    private String rackNo;

    @Column(name = "item_code", length = 18, nullable = false)
    private String itemCode;

    /** 입고 로트 날짜 (FIFO 정렬 기준) */
    @Column(name = "lot_date", nullable = false)
    private LocalDate lotDate;

    /** 포장 유형 (S/B/T) */
    @Enumerated(EnumType.STRING)
    @Column(name = "bag_type", length = 1, nullable = false)
    private BagType bagType;

    /** 적재 유형 코드 */
    @Column(name = "load_type", length = 2, nullable = false)
    private String loadType;

    /** 재고 수량 (kg 단위 소수점 지원) */
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false, length = 2)
    private StockStatus stockStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false, length = 1)
    private ReservationStatus reservationStatus;

    private Stock(String storageId, String rackNo, String itemCode, LocalDate lotDate,
                  BagType bagType, String loadType, BigDecimal quantity) {
        this.storageId = storageId;
        this.rackNo = rackNo;
        this.itemCode = itemCode;
        this.lotDate = lotDate;
        this.bagType = bagType;
        this.loadType = loadType;
        this.quantity = quantity;
        this.stockStatus = StockStatus.NORMAL;
        this.reservationStatus = ReservationStatus.NONE;
    }

    /**
     * 재고 생성 팩토리 메서드.
     * 초기 상태: NORMAL / NONE
     */
    public static Stock create(String storageId, String rackNo, String itemCode, LocalDate lotDate,
                               BagType bagType, String loadType, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UnsupportedOperationException("재고수량은 양수여야 합니다.");
        }
        return new Stock(storageId, rackNo, itemCode, lotDate, bagType, loadType, quantity);
    }

    // ========== 도메인 메서드 (직접 구현하세요) ==========

    /**
     * 출고 예약: NONE → RESERVED
     * 전제조건: reservationStatus == NONE
     */
    public void reserve() {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 예약 해제: RESERVED → NONE
     * 전제조건: reservationStatus == RESERVED
     */
    public void release() {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 보류 처리: NORMAL → HOLD
     * 전제조건: stockStatus == NORMAL
     */
    public void hold() {
        // TODO (Wave 1 TDD)
        if(stockStatus != StockStatus.NORMAL) {
            throw new WmsStateException(ErrorCode.RACK_NOT_AVAILABLE);
        }
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 보류 해제: HOLD → NORMAL
     * 전제조건: stockStatus == HOLD
     */
    public void unhold() {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 위치 이동: storageId + rackNo 변경
     * 전제조건: stockStatus != HOLD
     */
    public void moveToLocation(String targetStorageId, String targetRackNo) {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 재고 합산: other.quantity를 this에 더함
     * 전제조건: other.itemCode == this.itemCode
     * 전제조건: stockStatus != HOLD
     */
    public void mergeWith(Stock other) {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 재고 분할: splitQty만큼 차감 후 새 Stock 반환
     * 전제조건: splitQty <= quantity, stockStatus != HOLD
     */
    public Stock splitQuantity(BigDecimal splitQty) {
        // TODO (Wave 1 TDD)
        throw new UnsupportedOperationException("TODO");
    }
}
