package dev.gyungmean.newwms.inventory.repository;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import dev.gyungmean.newwms.master.domain.BagType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("품목코드로 재고 전체 조회")
    void findByItemCode() {
        stockRepository.save(stock("ITEM001", "STRG01", "08010101", LocalDate.now(), BigDecimal.valueOf(10)));
        stockRepository.save(stock("ITEM001", "STRG01", "08010102", LocalDate.now(), BigDecimal.valueOf(20)));
        stockRepository.save(stock("ITEM002", "STRG01", "08010103", LocalDate.now(), BigDecimal.valueOf(30)));

        List<Stock> result = stockRepository.findByItemCode("ITEM001");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("storageId + rackNo 로 재고 조회")
    void findByStorageIdAndRackNo() {
        stockRepository.save(stock("ITEM001", "STRG01", "08010101", LocalDate.now(), BigDecimal.valueOf(10)));
        stockRepository.save(stock("ITEM002", "STRG01", "08010101", LocalDate.now(), BigDecimal.valueOf(20)));
        stockRepository.save(stock("ITEM001", "STRG02", "08010101", LocalDate.now(), BigDecimal.valueOf(30)));

        List<Stock> result = stockRepository.findByStorageIdAndRackNo("STRG01", "08010101");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("FIFO 정렬 — lotDate ASC 순서 보장")
    void findByItemCodeOrderByLotDateAsc() {
        LocalDate day1 = LocalDate.of(2024, 1, 1);
        LocalDate day2 = LocalDate.of(2024, 2, 1);
        LocalDate day3 = LocalDate.of(2024, 3, 1);

        stockRepository.save(stock("ITEM001", "STRG01", "08010103", day3, BigDecimal.valueOf(10)));
        stockRepository.save(stock("ITEM001", "STRG01", "08010101", day1, BigDecimal.valueOf(10)));
        stockRepository.save(stock("ITEM001", "STRG01", "08010102", day2, BigDecimal.valueOf(10)));

        List<Stock> result = stockRepository.findByItemCodeOrderByLotDateAsc("ITEM001");

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getLotDate()).isEqualTo(day1);
        assertThat(result.get(1).getLotDate()).isEqualTo(day2);
        assertThat(result.get(2).getLotDate()).isEqualTo(day3);
    }

    @Test
    @DisplayName("HOLD 상태 제외 FIFO 조회")
    void findByItemCodeExcludeHold() {
        Stock normal1 = stockRepository.save(stock("ITEM001", "STRG01", "08010101", LocalDate.of(2024, 1, 1), BigDecimal.valueOf(10)));
        Stock holdStock = stockRepository.save(stock("ITEM001", "STRG01", "08010102", LocalDate.of(2024, 2, 1), BigDecimal.valueOf(10)));
        Stock normal2 = stockRepository.save(stock("ITEM001", "STRG01", "08010103", LocalDate.of(2024, 3, 1), BigDecimal.valueOf(10)));

        holdStock.hold();
        stockRepository.save(holdStock);

        List<Stock> result = stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc("ITEM001", StockStatus.HOLD);

        assertThat(result).hasSize(2);
        assertThat(result).noneMatch(s -> s.getStockStatus() == StockStatus.HOLD);
        assertThat(result.get(0).getLotDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    private Stock stock(String itemCode, String storageId, String rackNo, LocalDate lotDate, BigDecimal qty) {
        return Stock.create(storageId, rackNo, itemCode, lotDate, BagType.S, "L1", qty);
    }
}
