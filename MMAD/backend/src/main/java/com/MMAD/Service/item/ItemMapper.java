package com.MMAD.Service.item;

import org.springframework.stereotype.Component;

import com.MMAD.dto.item.ItemDTO;
import com.MMAD.model.item.Item;

@Component
public class ItemMapper {

    public ItemDTO toDTO(Item item) {
        return ItemDTO.fromEntity(item);
    }


    public Item toEntity(ItemDTO dto) {

        if (dto == null) {
            return null;
        }

        return dto.toEntity();
    }
}