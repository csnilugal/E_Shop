package com.ecommerce.controller;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    // GET ALL PRODUCTS
    @GetMapping
    public List<Product> getProducts() {

        return productService.getAllProducts();
    }

    // SEARCH PRODUCTS
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam String keyword) {

        return productService
                .searchProducts(keyword);
    }

    // CATEGORY FILTER
    @GetMapping("/category/{name}")
    public List<Product> getByCategory(
            @PathVariable String name) {

        return productService
                .getProductsByCategory(name);
    }

    // OWNER PRODUCTS
    @GetMapping("/owner/{ownerId}")
    public List<Product> getOwnerProducts(
            @PathVariable Long ownerId) {

        return productService
                .getProductsByOwner(ownerId);
    }

    // ADD PRODUCT
    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProduct(

            @RequestPart("product")
            ProductRequest request,

            @RequestPart("image")
            MultipartFile image) {

        return productService
                .addProduct(request, image);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);
    }

    // UPDATE PRODUCT AVAILABILITY
    @PutMapping("/{id}/availability")
    public Product updateAvailability(

            @PathVariable Long id,

            @RequestParam Boolean available) {

        return productService
                .updateAvailability(
                        id,
                        available);
    }
}