package com.MMAD.Service.item;

import jakarta.persistence.EntityNotFoundException; // Keep for update/delete/findById/etc. if needed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.dto.item.ItemDTO;
import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.Item;
import com.MMAD.model.item.MusicProvider;
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
    public Item addItem(Item item) {

        if (item.getSourceId() == null || item.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Source ID cannot be empty or null for a new Item.");
        }

        Optional<Item> existingItem = itemRepo.findByProviderAndSourceId(
                item.getProvider(),
                item.getSourceId());

        if (existingItem.isPresent()) {
            System.out.println(
                    "ItemService: Item with source ID '"
                            + item.getSourceId()
                            + "' already exists. Returning existing item.");

            return existingItem.get();
        }

        System.out.println(
                "ItemService: Saving new item with source ID '"
                        + item.getSourceId() + "'.");

        Item resolvedItem = itemResolver.resolve(item);

        return itemRepo.save(resolvedItem);
    }

    // READ
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepo.findAll();
        return items.stream()
                .map(ItemDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> getItemById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieval.");
        }

        Optional<Item> itemFound = itemRepo.findById(id);
        return itemFound.map(ItemDTO::fromEntity);
    }

    public Optional<Item> getItemEntityById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieval.");
        }

        return itemRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> getItemByProviderAndSourceId(MusicProvider provider, String sourceId) { // --- MODIFIED:
                                                                                                     // Return
                                                                                                     // Optional<Item>
                                                                                                     // ---
        // 1. Input validation for sourceId
        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Source ID cannot be null or empty for retrieval.");
        }
        Optional<Item> itemFound = itemRepo.findByProviderAndSourceId(provider, sourceId);

        // Map the Optional<Item> to Optional<ItemDTO> using map()
        return itemFound.map(ItemDTO::fromEntity);
    }

    // UPDATE
    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO itemDetails) {
        // 1. Basic input validation for ID and updated details object
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for update.");
        }
        if (itemDetails == null) {
            throw new IllegalArgumentException("Updated item details cannot be null.");
        }

        // 2. Validate the sourceId from itemDetails
        if (itemDetails.getSourceId() == null || itemDetails.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null in updated item details.");
        }

        // 3. Retrieve the existing item using the provided ID
        Item existingItem = itemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        // 4. Check for sourceId uniqueness, excluding the current item being updated
        if (!existingItem.getSourceId().equals(itemDetails.getSourceId())) {
            Optional<Item> duplicateSourceIdItem = itemRepo.findByProviderAndSourceId(itemDetails.getProvider(),
                    itemDetails.getSourceId());

            if (duplicateSourceIdItem.isPresent() && !duplicateSourceIdItem.get().getId().equals(id)) {
                throw new RuntimeException(
                        "Another item already exists with source ID '" + itemDetails.getSourceId() + "'.");
            }
        }

        // 5. Apply updates to the existing item entity
        existingItem.setSourceId(itemDetails.getSourceId());
        existingItem.setName(itemDetails.getName());
        existingItem.setImageURL(itemDetails.getImageURL());
        // Add any other updatable fields as needed

        // 6. Save the updated item and map to DTO
        Item updatedItem = itemRepo.save(existingItem);
        return ItemDTO.fromEntity(updatedItem);
    }

    // DELETE
    @Transactional
    public void deleteItem(Long id) {
        // 1. Input validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for deletion.");
        }
        // 2. Existence check before deleting
        // Still throwing EntityNotFoundException here, as you cannot delete a
        // non-existent item.
        if (!itemRepo.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        itemRepo.deleteById(id);
    }


}
