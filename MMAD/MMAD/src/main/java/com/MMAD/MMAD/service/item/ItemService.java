// File: com/MMAD/MMAD/service/item/ItemService.java
package com.MMAD.MMAD.service.item; // <--- Ensure this package is correct

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.repo.item.ItemRepo; // <--- Ensure this import is correct
import jakarta.persistence.EntityNotFoundException;
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
    public Item saveItem(Item item) {
        return itemRepo.save(item);
    }

    //READ    
    
    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
    @Transactional(readOnly = true)
    public Optional<Item>  getItemById(Long id) {
        return itemRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItemBySourceId(String sourceId) {
        // Corrected: Now directly uses the efficient findBySourceId method from ItemRepo
        return itemRepo.findBySourceId(sourceId);
    }


    //UPDATE
    @Transactional
    public Item updateItem(Long id, Item itemDetails) {
        Item existingItem = itemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        existingItem.setSourceId(itemDetails.getSourceId());
        existingItem.setName(itemDetails.getName());
        existingItem.setImageURL(itemDetails.getImageURL());

        return itemRepo.save(existingItem);
    }

    //DELETE
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepo.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        itemRepo.deleteById(id);
    }
}