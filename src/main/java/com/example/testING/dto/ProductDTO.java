package com.example.testING.dto;

public record ProductDTO(Long id, String name, String description, String producer, double price, Long categoryId) {
}