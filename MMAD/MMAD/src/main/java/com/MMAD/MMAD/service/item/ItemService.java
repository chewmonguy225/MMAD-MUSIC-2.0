// File: com/MMAD/MMAD/service/item/ItemService.java
package com.MMAD.MMAD.service.item;

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.ItemDTO;
import com.MMAD.MMAD.repo.item.ItemRepo;
import jakarta.persistence.EntityNotFoundException; // Keep for update/delete/findById/etc. if needed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepo itemRepo;

    public ItemService(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    // CREATE
    @Transactional
    public ItemDTO addItem(Item item) {
        // 1. Validate that sourceId is provided and not empty
        if (item.getSourceId() == null || item.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null for a new Item.");
        }

        // 2. Check if an Item with the given sourceId already exists
        Optional<Item> existingItem = itemRepo.findBySourceId(item.getSourceId());
        Item savedOrExistingItem;

        if (existingItem.isPresent()) {
            // --- MODIFIED LOGIC: If an Item with this sourceId already exists, RETURN IT
            // ---
            System.out.println("ItemService: Item with source ID '" + item.getSourceId()
                    + "' already exists. Returning existing item.");
            savedOrExistingItem = existingItem.get();
        } else {
            // If no existing Item found by sourceId, proceed with saving the new Item
            System.out.println("ItemService: No existing item found with source ID '" + item.getSourceId()
                    + "'. Saving new item.");
            itemRepo.save(item);
            savedOrExistingItem = existingItem.get();
        }

        return ItemDTO.fromEntity(savedOrExistingItem);
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

        // Map the Optional<Item> to Optional<ItemDTO> using map()
        return itemFound.map(ItemDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> getItemBySourceId(String sourceId) { // --- MODIFIED: Return Optional<Item> ---
        // 1. Input validation for sourceId
        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Source ID cannot be null or empty for retrieval.");
        }
        Optional<Item> itemFound = itemRepo.findBySourceId(sourceId);

        // Map the Optional<Item> to Optional<ItemDTO> using map()
        return itemFound.map(ItemDTO::fromEntity);
    }


    //UPDATE
    @Transactional
    public ItemDTO updateItem(Long id, Item itemDetails) {
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
            Optional<Item> duplicateSourceIdItem = itemRepo.findBySourceId(itemDetails.getSourceId());
    
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