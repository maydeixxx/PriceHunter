package com.PriceHunter.ProductService.models;

import com.PriceHunter.ProductService.models.enums.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Shop shop;

    private Boolean active;

    @Column(nullable = false)
    private BigDecimal lastPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
