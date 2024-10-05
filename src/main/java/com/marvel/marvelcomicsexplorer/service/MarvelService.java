package com.marvel.marvelcomicsexplorer.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.marvel.marvelcomicsexplorer.model.MarvelItem;

@Service
public class MarvelService {

    private static final Logger logger = LoggerFactory.getLogger(MarvelService.class);

    @Value("${marvel.api.public-key}")
    private String publicKey;

    @Value("${marvel.api.private-key}")
    private String privateKey;

    private final WebClient webClient;

    public MarvelService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://gateway.marvel.com/v1/public").build();
    }

    public List<MarvelItem> searchMarvelItems(String category, String query) {
        long timestamp = System.currentTimeMillis();
        String hash = generateHash(timestamp, privateKey, publicKey);

        logger.info("Searching for {} with query: {}", category, query);

        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + category)
                        .queryParam("apikey", publicKey)
                        .queryParam("ts", timestamp)
                        .queryParam("hash", hash)
                        .queryParam("nameStartsWith", query)
                        .queryParam("limit", 20)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        logger.info("Received response: {}", response);

        List<MarvelItem> items = new ArrayList<>();
        if (response != null && response.has("data") && response.get("data").has("results")) {
            JsonNode results = response.get("data").get("results");
            for (JsonNode result : results) {
                MarvelItem item = new MarvelItem();
                item.setId(result.get("id").asLong());
                item.setName(result.has("name") ? result.get("name").asText() : null);
                item.setTitle(result.has("title") ? result.get("title").asText() : null);
                item.setDescription(result.has("description") ? result.get("description").asText() : null);
                if (result.has("thumbnail")) {
                    JsonNode thumbnail = result.get("thumbnail");
                    MarvelItem.Thumbnail itemThumbnail = new MarvelItem.Thumbnail();
                    itemThumbnail.setPath(thumbnail.get("path").asText());
                    itemThumbnail.setExtension(thumbnail.get("extension").asText());
                    item.setThumbnail(itemThumbnail);
                }
                items.add(item);
            }
        }

        // If no results found with nameStartsWith, try a more flexible search
        if (items.isEmpty()) {
            logger.info("No results found with nameStartsWith, trying a more flexible search");
            return searchMarvelItemsFlexible(category, query);
        }

        return items;
    }

    private List<MarvelItem> searchMarvelItemsFlexible(String category, String query) {
        long timestamp = System.currentTimeMillis();
        String hash = generateHash(timestamp, privateKey, publicKey);

        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + category)
                        .queryParam("apikey", publicKey)
                        .queryParam("ts", timestamp)
                        .queryParam("hash", hash)
                        .queryParam("limit", 100)  // Increase limit for better chances of finding a match
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        List<MarvelItem> items = new ArrayList<>();
        if (response != null && response.has("data") && response.get("data").has("results")) {
            JsonNode results = response.get("data").get("results");
            String lowercaseQuery = query.toLowerCase();
            for (JsonNode result : results) {
                String name = result.has("name") ? result.get("name").asText().toLowerCase() : "";
                String title = result.has("title") ? result.get("title").asText().toLowerCase() : "";
                if (name.contains(lowercaseQuery) || title.contains(lowercaseQuery)) {
                    MarvelItem item = new MarvelItem();
                    item.setId(result.get("id").asLong());
                    item.setName(result.has("name") ? result.get("name").asText() : null);
                    item.setTitle(result.has("title") ? result.get("title").asText() : null);
                    item.setDescription(result.has("description") ? result.get("description").asText() : null);
                    if (result.has("thumbnail")) {
                        JsonNode thumbnail = result.get("thumbnail");
                        MarvelItem.Thumbnail itemThumbnail = new MarvelItem.Thumbnail();
                        itemThumbnail.setPath(thumbnail.get("path").asText());
                        itemThumbnail.setExtension(thumbnail.get("extension").asText());
                        item.setThumbnail(itemThumbnail);
                    }
                    items.add(item);
                }
            }
        }

        return items;
    }

    private String generateHash(long timestamp, String privateKey, String publicKey) {
        try {
            String input = timestamp + privateKey + publicKey;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}