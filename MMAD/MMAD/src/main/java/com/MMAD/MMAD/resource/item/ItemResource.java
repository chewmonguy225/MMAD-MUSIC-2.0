package com.MMAD.MMAD.resource.item;

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.service.item.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemResource {

    private final ItemService itemService;

    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }   

    //CREATE
    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody Item item) {
        try {
            Item newItem = itemService.saveItem(item);
            return new ResponseEntity<>(newItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ
    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id) {
        try {
            // Get the Optional from the service
            Optional<Item> optionalItem = itemService.getItemById(id);

            // Unwrap the Optional:
            // If the Optional contains an Item, return it with HttpStatus.OK.
            // If it's empty, orElseThrow will throw EntityNotFoundException.
            Item item = optionalItem.orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

            return new ResponseEntity<>(item, HttpStatus.OK); // Return the actual Item object
        } catch (EntityNotFoundException e) {
            // This catch block will now correctly handle the EntityNotFoundException
            // thrown by orElseThrow() if the item is not found.
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }


    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<?> getItemBySourceId(@PathVariable("source_id") String sourceId) { // Changed return type to
                                                                                             // ResponseEntity<?>
        try {
            Item item = itemService.getItemBySourceId(sourceId)
                    .orElseThrow(() -> new EntityNotFoundException("Item not found with Source ID: " + sourceId));
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Now this returns ResponseEntity<String>, which is compatible with
            // ResponseEntity<?>
            return new ResponseEntity<>("Failed to find item by Source ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") Long id, @RequestBody Item itemDetails) {
        try {
            Item updatedItem = itemService.updateItem(id, itemDetails);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // Corrected: Use ResponseEntity.notFound().build() for 404 with no body
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable("id") Long id) {
        try {
            itemService.deleteItem(id);
            // Corrected: Use ResponseEntity.noContent().build() for 204 No Content
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            // Corrected: Use ResponseEntity.notFound().build() for 404 with no body
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "Item resource works!";
    }
}