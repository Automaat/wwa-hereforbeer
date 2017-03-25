package com.hereforbeer.controllers;

import com.hereforbeer.search_phrases.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categories")
class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping(params = {"searchPhrase"})
    ResponseEntity<?> getCategories(@RequestParam("searchPhrase") String searchPhrase) {
        return categoriesService.getCategories(searchPhrase)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
