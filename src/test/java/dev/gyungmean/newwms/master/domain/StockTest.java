package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.ReservationStatus;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class StockTest {

    @Test
    @DisplayName("재고 엔티티 생성")
    void createStock() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        assertThat(stock.getStorageId()).isEqualTo("STRG01");
        assertThat(stock.getRackNo()).isEqualTo("08010101");
        assertThat(stock.getStockStatus()).isEqualTo(StockStatus.NORMAL);
        assertThat(stock.getReservationStatus()).isEqualTo(ReservationStatus.NONE);
    }

    @Test
    @DisplayName("재고 예약")
    void reserveStock() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        stock.reserve();
        assertThat(stock.getReservationStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    @DisplayName("재고 예약 취소")
    void release() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        stock.reserve();
        stock.release();

        assertThat(stock.getReservationStatus()).isEqualTo(ReservationStatus.NONE);
    }

    @Test
    @DisplayName("재고 보류")
    void hold() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        stock.hold();

        assertThat(stock.getStockStatus()).isEqualTo(StockStatus.HOLD);
    }

    @Test
    @DisplayName("재고 보류 해제")
    void unhold() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        stock.hold();
        stock.unhold();

        assertThat(stock.getStockStatus()).isEqualTo(StockStatus.NORMAL);
    }

    @Test
    @DisplayName("랙번호 이동")
    void moveLocation() {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        stock.moveToLocation("STRG02", "09010101");

        assertThat(stock.getStorageId()).isEqualTo("STRG02");
        assertThat(stock.getRackNo()).isEqualTo("09010101");
    }

    @Test
    @DisplayName("재고 합산")
    void mergeStock() {
        Stock stock1 = Stock.create("STRG01", "08010101", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));
        Stock stock2 = Stock.create("STRG01", "08010102", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(1));

        stock1.mergeWith(stock2);

        assertThat(stock1.getQuantity()).isEqualTo(BigDecimal.valueOf(12));
    }

    @Test
    @DisplayName("재고 분할")
    void splitQuantity() {
        Stock stock1 = Stock.create("STRG01", "00000000", "ITEM0001", LocalDate.now(), BagType.S, "L1",
            BigDecimal.valueOf(11));

        Stock newStock = stock1.splitQuantity(BigDecimal.valueOf(1));

        assertThat(stock1.getQuantity()).isEqualTo(BigDecimal.valueOf(10));
        assertThat(newStock.getQuantity()).isEqualTo(BigDecimal.valueOf(1));
    }
}
