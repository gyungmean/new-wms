package dev.gyungmean.newwms.inventory.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HoldOrderTest {

    @Test
    @DisplayName("보류 지시 생성 - active=true, holdAt 설정")
    void create() {
        HoldOrder holdOrder = HoldOrder.create(1L, "ITEM0001", "품질 이상");

        assertThat(holdOrder.getStockId()).isEqualTo(1L);
        assertThat(holdOrder.getItemCode()).isEqualTo("ITEM0001");
        assertThat(holdOrder.getReason()).isEqualTo("품질 이상");
        assertThat(holdOrder.isActive()).isTrue();
        assertThat(holdOrder.getHoldAt()).isNotNull();
        assertThat(holdOrder.getUnholdAt()).isNull();
    }

    @Test
    @DisplayName("보류 해제 - active=false, unholdAt 설정")
    void deactivate() {
        HoldOrder holdOrder = HoldOrder.create(1L, "ITEM0001", "품질 이상");

        holdOrder.deactivate();

        assertThat(holdOrder.isActive()).isFalse();
        assertThat(holdOrder.getUnholdAt()).isNotNull();
    }
}
