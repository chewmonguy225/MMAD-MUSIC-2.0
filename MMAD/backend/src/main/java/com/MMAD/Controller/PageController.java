package com.MMAD.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.Service.page.ItemPageService;
import com.MMAD.dto.page.ItemPageDTO;

@RestController
@RequestMapping("/page")
public class PageController {

    private final ItemPageService itemPageService;

    public PageController(ItemPageService itemPageService) {
        this.itemPageService = itemPageService;
    }

    @GetMapping("/item/{id}")
    public ItemPageDTO getItemPage(@PathVariable Long id) {
        return itemPageService.getItemPage(id);
        
    }
}