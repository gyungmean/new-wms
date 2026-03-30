package dev.gyungmean.newwms.inventory.service;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.master.domain.BagType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FifoCandidateSelectorTest {

    private FifoCandidateSelector selector;

    @BeforeEach
    void setUp() {
        selector = new FifoCandidateSelector();
    }

    @Test
    @DisplayName("요청 수량을 단일 재고로 충족")
    void select_singleStock() {
        Stock stock = stock("ITEM001", LocalDate.of(2024, 1, 1), BigDecimal.valueOf(100));

        FifoCandidateSelector.FifoSelection result = selector.select(List.of(stock), BigDecimal.valueOf(50));

        assertThat(result.allocations()).hasSize(1);
        assertThat(result.allocations().get(0).allocatedQty()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.totalAllocated()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }

    @Test
    @DisplayName("요청 수량을 여러 재고에서 FIFO 순서로 충족")
    void select_multipleStocks() {
        Stock old = stock("ITEM001", LocalDate.of(2024, 1, 1), BigDecimal.valueOf(30));
        Stock mid = stock("ITEM001", LocalDate.of(2024, 2, 1), BigDecimal.valueOf(30));
        Stock recent = stock("ITEM001", LocalDate.of(2024, 3, 1), BigDecimal.valueOf(30));

        FifoCandidateSelector.FifoSelection result = selector.select(List.of(old, mid, recent), BigDecimal.valueOf(50));

        assertThat(result.allocations()).hasSize(2);
        assertThat(result.allocations().get(0).stock()).isEqualTo(old);
        assertThat(result.allocations().get(0).allocatedQty()).isEqualByComparingTo(BigDecimal.valueOf(30));
        assertThat(result.allocations().get(1).stock()).isEqualTo(mid);
        assertThat(result.allocations().get(1).allocatedQty()).isEqualByComparingTo(BigDecimal.valueOf(20));
        assertThat(result.totalAllocated()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }

    @Test
    @DisplayName("요청 수량과 재고가 정확히 일치")
    void select_exactMatch() {
        Stock stock = stock("ITEM001", LocalDate.of(2024, 1, 1), BigDecimal.valueOf(100));

        FifoCandidateSelector.FifoSelection result = selector.select(List.of(stock), BigDecimal.valueOf(100));

        assertThat(result.allocations()).hasSize(1);
        assertThat(result.totalAllocated()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    @DisplayName("재고 수량 부족 시 예외")
    void select_insufficientStock() {
        Stock stock = stock("ITEM001", LocalDate.of(2024, 1, 1), BigDecimal.valueOf(10));

        assertThatThrownBy(() -> selector.select(List.of(stock), BigDecimal.valueOf(50)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("재고 목록이 비어있으면 예외")
    void select_emptyList() {
        assertThatThrownBy(() -> selector.select(List.of(), BigDecimal.valueOf(10)))
                .isInstanceOf(IllegalStateException.class);
    }

    private Stock stock(String itemCode, LocalDate lotDate, BigDecimal qty) {
        return Stock.create("STRG01", "08010101", itemCode, lotDate, BagType.S, "L1", qty);
    }
}
