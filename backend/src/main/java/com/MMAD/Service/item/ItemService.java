package com.MMAD.Service.item;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.dto.item.ItemAddRequestDTO;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.entity.item.Item;
import com.MMAD.entity.item.MusicProvider;
import com.MMAD.repo.item.ItemRepo;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepo itemRepo;
    private final ItemResolver itemResolver;

    public ItemService(
            ItemRepo itemRepo,
            ItemResolver itemResolver) {

        this.itemRepo = itemRepo;
        this.itemResolver = itemResolver;
    }


    // CREATE
    @Transactional
    public Item addItem(ItemAddRequestDTO request) {

        Item item = itemResolver.resolveSpotifyItem(
                request.getType(),
                request.getSourceId(),
                request.getProvider());

        return itemResolver.resolve(item);
    }


    // READ ALL
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {

        List<Item> items = itemRepo.findAll();

        return items.stream()
                .map(ItemDTO::fromEntity)
                .toList();
    }


    // READ BY ID DTO
    @Transactional(readOnly = true)
    public Optional<ItemDTO> getItemById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null or non-positive for retrieval.");
        }

        return itemRepo.findById(id)
                .map(ItemDTO::fromEntity);
    }


    // READ ENTITY
    public Optional<Item> getItemEntityById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null or non-positive for retrieval.");
        }

        return itemRepo.findById(id);
    }


    // READ + REFRESH FROM EXTERNAL PROVIDER
    @Transactional
    public Item getItemWithRefresh(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null or non-positive for retrieval.");
        }

        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with ID: " + id));

        return itemResolver.refresh(item);
    }


    // READ BY PROVIDER + SOURCE ID
    @Transactional(readOnly = true)
    public Optional<ItemDTO> getItemByProviderAndSourceId(
            MusicProvider provider,
            String sourceId) {

        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Item Source ID cannot be null or empty for retrieval.");
        }

        return itemRepo
                .findByProviderAndSourceId(provider, sourceId)
                .map(ItemDTO::fromEntity);
    }


    // UPDATE
    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO itemDetails) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null or non-positive for update.");
        }

        if (itemDetails == null) {
            throw new IllegalArgumentException(
                    "Updated item details cannot be null.");
        }

        Item existingItem = itemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with ID: " + id));


        existingItem.setSourceId(itemDetails.getSourceId());
        existingItem.setName(itemDetails.getName());
        existingItem.setImageURL(itemDetails.getImageURL());


        Item updatedItem = itemRepo.save(existingItem);

        return ItemDTO.fromEntity(updatedItem);
    }


    // DELETE
    @Transactional
    public void deleteItem(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null or non-positive for deletion.");
        }


        if (!itemRepo.existsById(id)) {
            throw new EntityNotFoundException(
                    "Item not found with ID: " + id);
        }


        itemRepo.deleteById(id);
    }

}