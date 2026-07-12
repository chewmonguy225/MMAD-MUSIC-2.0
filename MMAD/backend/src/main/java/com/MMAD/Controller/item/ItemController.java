package com.MMAD.Controller.item;

import jakarta.persistence.EntityNotFoundException; // Keep this, as ItemService still throws it
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.dto.item.ItemAddRequestDTO;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.entity.item.Item;
import com.MMAD.entity.item.MusicProvider;
import com.MMAD.Service.item.ItemMapper;
import com.MMAD.Service.item.ItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(
            ItemService itemService,
            ItemMapper itemMapper) {
        this.itemService = itemService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(
            @RequestBody ItemAddRequestDTO request) {

        Item saved = itemService.addItem(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ItemDTO.fromEntity(saved));
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getItemById(@PathVariable("id") Long id) {
        try {
            Optional<ItemDTO> itemDtoOpt = itemService.getItemById(id);
            if (itemDtoOpt.isPresent()) {
                return new ResponseEntity<>(itemDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find item by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findSource/{provider}/{source_id}")
    public ResponseEntity<?> getItemBySourceId(
            @PathVariable("provider") MusicProvider provider,
            @PathVariable("source_id") String sourceId) {
        try {

            Optional<ItemDTO> itemDtoOpt = itemService.getItemByProviderAndSourceId(provider, sourceId);

            if (itemDtoOpt.isPresent()) {
                return new ResponseEntity<>(itemDtoOpt.get(), HttpStatus.OK);
            }

            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Failed to find item by Source ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(
            @PathVariable("id") Long id,
            @RequestBody ItemDTO itemDetails) {
        try {
            ItemDTO updatedItemDto = itemService.updateItem(id, itemDetails);
            return new ResponseEntity<>(updatedItemDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable("id") Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            // Catches ID validation errors
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            // Catches if the item to delete is not found
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Generic catch-all for other unexpected errors
            return new ResponseEntity<>("Failed to delete item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "Item resource works!";
    }
}