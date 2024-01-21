package com.example.testING.dto;

import java.time.LocalDate;

public record StockDTO(Long id, Long productId, int quantity, LocalDate lastTimeUpdated) {

}