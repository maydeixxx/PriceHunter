package com.PriceHunter.ProductService.models.exceptions;

public class ProductUpdateException extends RuntimeException {
    public ProductUpdateException(String message) {
        super(message);
    }
}
