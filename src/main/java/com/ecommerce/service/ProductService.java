package com.ecommerce.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> searchProducts(String keyword) {

        if(keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }

        return productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrStoreNameContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword
                );
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory_Name(category);
    }

    public Product addProduct(
            ProductRequest request,
            MultipartFile image) {

        Product product = new Product();

        product.setName(request.getName());

        product.setPrice(request.getPrice());

        product.setOwnerId(request.getOwnerId());

        product.setQuantity(request.getQuantity());

        product.setDescription(
                request.getDescription());

        product.setStoreName(
                request.getStoreName());

        product.setAvailable(true);

        // IMAGE URL
        product.setImageUrl(
                "/images/" +
                        image.getOriginalFilename());

        // CATEGORY
        Category category =
                categoryRepository.findById(
                                request.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    public List<Product> getProductsByOwner(
            Long ownerId) {

        return productRepository
                .findByOwnerId(ownerId);
    }

    public Product updateAvailability(
            Long productId,
            Boolean available) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));

        product.setAvailable(available);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}