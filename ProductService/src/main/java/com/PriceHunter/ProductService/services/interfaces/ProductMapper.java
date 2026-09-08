package com.PriceHunter.ProductService.services.interfaces;

import com.PriceHunter.ProductService.models.Product;
import com.PriceHunter.ProductService.models.domain.ProductDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product domainToEntity(ProductDomain domain);

    ProductDomain entityToDomain(Product entity);
}
