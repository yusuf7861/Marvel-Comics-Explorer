package com.marvel.marvelcomicsexplorer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.marvel.marvelcomicsexplorer.model.MarvelItem;
import com.marvel.marvelcomicsexplorer.service.MarvelService;

@Controller
public class MarvelController {

    @Autowired
    private MarvelService marvelService;

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

    
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<MarvelItem>> search(@RequestParam String category, @RequestParam(required = false) String query) {
        try {
            List<MarvelItem> items = marvelService.searchMarvelItems(category, query);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
