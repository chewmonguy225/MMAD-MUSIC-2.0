package com.MMAD.MMAD.repo.item;

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Artist.Artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;



@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    Optional<Item> findItemById(Long id);
    Optional<Item> findBySourceId(String sourceId);
    Optional<Item> findByName(String name);
}