package dev.gyungmean.newwms.master.controller.req;

import dev.gyungmean.newwms.master.domain.BagType;
import dev.gyungmean.newwms.master.domain.ItemKind;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemSearchReq {

    private String itemName;

    private ItemKind itemKind;

    private BagType bagType;
}
