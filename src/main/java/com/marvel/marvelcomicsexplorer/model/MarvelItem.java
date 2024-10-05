package com.marvel.marvelcomicsexplorer.model;

import lombok.Data;

@Data
public class MarvelItem {

    private long id;
    private String name;
    private String title;
    private String description;
    private Thumbnail thumbnail;

    @Data
    public static class Thumbnail {
        private String path;
        private String extension;
    }
}