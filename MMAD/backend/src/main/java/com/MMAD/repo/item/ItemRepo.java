package com.MMAD.repo.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.model.item.Item;
import com.MMAD.model.item.MusicProvider;

import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    Optional<Item> findByProviderAndSourceId(
            MusicProvider provider,
            String sourceId);

    Optional<Item> findByName(String name);
}