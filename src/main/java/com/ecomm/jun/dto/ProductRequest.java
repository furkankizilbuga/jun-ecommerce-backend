package com.ecomm.jun.dto;

import java.util.Set;

public record ProductRequest(String name, String imagePath, Double rating, Set<Long> categoryIds) {
}
