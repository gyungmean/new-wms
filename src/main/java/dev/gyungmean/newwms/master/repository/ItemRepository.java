package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.BagType;
import dev.gyungmean.newwms.master.domain.Item;
import dev.gyungmean.newwms.master.domain.ItemKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByItemKind(ItemKind itemKind);

    List<Item> findByBagType(BagType bagType);

    List<Item> findByItemNameContaining(String keyword);
}
