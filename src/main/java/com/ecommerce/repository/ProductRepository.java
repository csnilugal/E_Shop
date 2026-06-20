package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwnerId(Long ownerId);
    List<Product> findByCategory_Name(String categoryName);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrStoreNameContainingIgnoreCase(String keyword, String keyword1, String keyword2, String keyword3);
}
