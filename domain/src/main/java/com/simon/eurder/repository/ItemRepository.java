package com.simon.eurder.repository;

import com.simon.eurder.domain.item.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAll();
    Optional<Item> findByExternalId(String id);


}
