package com.MMAD.dto.item;

import com.MMAD.entity.item.MusicProvider;

public class ItemAddRequestDTO {

    private String sourceId;
    private String type;
    private MusicProvider provider;


    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public MusicProvider getProvider() {
        return provider;
    }


    public void setProvider(MusicProvider provider) {
        this.provider = provider;
    }
}