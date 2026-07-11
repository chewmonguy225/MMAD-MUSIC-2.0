package com.MMAD.dto;

import java.util.List;
import com.MMAD.dto.search.SearchResultDTO;

public record SearchResponse(
        List<SearchResultDTO> items
) {}