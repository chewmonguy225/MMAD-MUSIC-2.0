package com.MMAD.repo.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.model.item.Item;

import java.util.Optional;



@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    Optional<Item> findItemById(Long id);
    Optional<Item> findBySourceId(String sourceId);
    Optional<Item> findByName(String name);
}