package dev.gyungmean.newwms.master.controller.req;

import dev.gyungmean.newwms.master.domain.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ItemCreateReq {

    @NotBlank
    private String itemCode;

    @NotBlank
    private String itemName;

    @NotNull
    private ItemKind itemKind;

    private String zoneCode;

    @NotNull
    private ItemUnit itemUnit;

    private ItemSize itemSize;

    private BigDecimal weightUnit;

    private BagType bagType;
}
