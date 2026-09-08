package com.PriceHunter.ProductService.models.domain;

import com.PriceHunter.ProductService.models.enums.Shop;
import com.PriceHunter.ProductService.models.exceptions.ProductArgsException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class ProductDomain {
    private final UUID productId;

    private final UUID userId;

    private final String url;
    private final String title;

    private final Shop shop;

    private Boolean active;

    private BigDecimal lastPrice;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private ProductDomain(UUID productId, UUID userId, String url, String title, Shop shop, Boolean active, BigDecimal lastPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.userId = userId;
        this.url = url;
        this.title = title;
        this.shop = shop;
        this.active = active;
        this.lastPrice = lastPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductDomain createProduct(UUID productId, UUID userId, String url, String title, Shop shop, BigDecimal lastPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (userId == null) {
            throw new ProductArgsException("User id cant be null");
        }

        if (url == null || url.isBlank()) {
            throw new ProductArgsException("Url cant be null or blank");
        }

        if (title == null || title.isBlank()) {
            throw new ProductArgsException("Title cant be null or blank");
        }

        if (shop == null) {
            throw new ProductArgsException("Shop is required");
        }

        return new ProductDomain(productId, userId, url, title, shop, true, lastPrice, createdAt, updatedAt);
    }

    public void updatePrice(BigDecimal newPrice) {
        if (lastPrice.compareTo(newPrice) == 0) return;
        this.lastPrice = newPrice;
    }

    public void disable() {
        if (!this.active) throw new ProductArgsException("Product already disabled");
        this.active = false;
    }

    public void enable() {
        if (this.active) throw new ProductArgsException("Product already active");
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDomain that)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
