package com.MMAD.dto.user;
import com.MMAD.dto.item.ItemDTO;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("user")
public class UserItemDTO extends ItemDTO {

    public UserItemDTO(Long id, String name, String imageURL) {
        super(id, null, name, imageURL);
    }
}