package com.PriceHunter.ProductService.services;

import com.PriceHunter.ProductService.models.Product;
import com.PriceHunter.ProductService.models.domain.ProductDomain;
import com.PriceHunter.ProductService.models.enums.TypeOfUpdate;
import com.PriceHunter.ProductService.models.exceptions.ProductNotFoundException;
import com.PriceHunter.ProductService.models.exceptions.ProductOwnerException;
import com.PriceHunter.ProductService.models.exceptions.ProductUpdateException;
import com.PriceHunter.ProductService.services.interfaces.ProductMapper;
import com.PriceHunter.ProductService.services.interfaces.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public void saveProduct(ProductDomain productDomain) {
        try {
            productRepository.save(productMapper.domainToEntity(productDomain));
        } catch (Exception e) {
            log.error("Error saving new product: {}", e.getMessage());
            throw new RuntimeException("Internal server error: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProduct(UUID productId, UUID userId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product [%s] not found".formatted(productId)));
            UUID productUserId = product.getUserId();

            if (userId.compareTo(productUserId) != 0) {
                throw new ProductOwnerException("You are not the owner of the product");
            }

            productRepository.delete(product);
        } catch (ProductNotFoundException | ProductOwnerException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void updateProduct(TypeOfUpdate typeOfUpdate, UUID productId, UUID userId) {
        try {
            ProductDomain product = productMapper.entityToDomain(productRepository
                    .findById(productId).orElseThrow(() -> new ProductNotFoundException("Product [%s] not found".formatted(productId))));
            UUID productUserId = product.getUserId();

            if (userId.compareTo(productUserId) != 0) {
                throw new ProductOwnerException("You are not the owner of the product");
            }

            switch (typeOfUpdate) {
                case DISABLE -> product.disable();
                case ENABLE -> product.enable();
                default -> throw new ProductUpdateException("Unknown type of product update: [%s]".formatted(typeOfUpdate));
            }

            productRepository.saveAndFlush(productMapper.domainToEntity(product));
        } catch (ProductNotFoundException | ProductOwnerException | ProductUpdateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    private void updateProductPrice() {
        //TODO обновление цены для товаров. Прогон по всему списку, для каждого товара свой запрос и парсинг новой цены
    }
}
