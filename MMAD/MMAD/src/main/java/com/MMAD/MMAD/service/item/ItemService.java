// File: com/MMAD/MMAD/service/item/ItemService.java
package com.MMAD.MMAD.service.item;

import com.MMAD.MMAD.model.Item.Item;
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

    //CREATE
    @Transactional
    public Item addItem(Item item) {
        // 1. Validate that sourceId is provided and not empty
        if (item.getSourceId() == null || item.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null for a new Item.");
        }

        // 2. Check if an Item with the given sourceId already exists
        Optional<Item> existingItem = itemRepo.findBySourceId(item.getSourceId());

        if (existingItem.isPresent()) {
            // --- MODIFIED LOGIC: If an Item with this sourceId already exists, RETURN IT ---
            System.out.println("ItemService: Item with source ID '" + item.getSourceId() + "' already exists. Returning existing item.");
            return existingItem.get(); 
        } else {
            // If no existing Item found by sourceId, proceed with saving the new Item
            System.out.println("ItemService: No existing item found with source ID '" + item.getSourceId() + "'. Saving new item.");
            return itemRepo.save(item);
        }
    }

    //READ

    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        // This method is fine as it returns an empty list if no items are found,
        // which is a standard way to represent "no results" for collections.
        return itemRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItemById(Long id) { // --- MODIFIED: Return Optional<Item> ---
        // 1. Input validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieval.");
        }
        // 2. Return Optional directly from repository, no longer throwing EntityNotFoundException here
        return itemRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItemBySourceId(String sourceId) { // --- MODIFIED: Return Optional<Item> ---
        // 1. Input validation for sourceId
        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Source ID cannot be null or empty for retrieval.");
        }
        // 2. Return Optional directly from repository, no longer throwing EntityNotFoundException here
        return itemRepo.findBySourceId(sourceId);
    }


    //UPDATE
    @Transactional
    public Item updateItem(Long id, Item itemDetails) {
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
        // Still throwing EntityNotFoundException here, as you cannot update a non-existent item.
        Item existingItem = itemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        // 4. Check for sourceId uniqueness, excluding the current item being updated
        // This check is only necessary if the sourceId is actually being changed
        if (!existingItem.getSourceId().equals(itemDetails.getSourceId())) {
            Optional<Item> duplicateSourceIdItem = itemRepo.findBySourceId(itemDetails.getSourceId());

            if (duplicateSourceIdItem.isPresent()) {
                // If a duplicate is found, ensure it's NOT the item we are currently updating.
                // If the found item has a different ID, then it's a true conflict.
                if (!duplicateSourceIdItem.get().getId().equals(id)) {
                    throw new RuntimeException("Another item already exists with source ID '" + itemDetails.getSourceId() + "'.");
                }
            }
        }

        // 5. Apply updates to the existing item entity
        existingItem.setSourceId(itemDetails.getSourceId());
        existingItem.setName(itemDetails.getName());
        existingItem.setImageURL(itemDetails.getImageURL());
        // Add any other fields that are part of the base Item and can be updated

        // 6. Save the updated existing item (this will perform an UPDATE)
        return itemRepo.save(existingItem);
    }

    //DELETE
    @Transactional
    public void deleteItem(Long id) {
        // 1. Input validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for deletion.");
        }
        // 2. Existence check before deleting
        // Still throwing EntityNotFoundException here, as you cannot delete a non-existent item.
        if (!itemRepo.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        itemRepo.deleteById(id);
    }
}