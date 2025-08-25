package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.Product;
import com.gtalent.tutor.models.Supplier;
import com.gtalent.tutor.models.User;
import com.gtalent.tutor.repositories.ProductRepository;
import com.gtalent.tutor.repositories.SupplierRepository;
import com.gtalent.tutor.requests.CreateProductRequest;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.requests.UpdateProductRequest;
import com.gtalent.tutor.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, SupplierRepository supplierRepository){
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products.stream().map(product -> {
            ProductResponse response = new ProductResponse(product);
            response.setSupplier(new SupplierResponse(product.getSupplier()));
            return response;
        }).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id ) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            ProductResponse response = new ProductResponse(product.get());
            response.setSupplier(new SupplierResponse(product.get().getSupplier()));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable int id, @RequestBody UpdateProductRequest request) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setName(request.getName());
            updatedProduct.setPrice(request.getPrice());
            updatedProduct.setQuantity(request.getQuantity());
            Product saveProduct = productRepository.save(updatedProduct);
            ProductResponse response = new ProductResponse(saveProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> createProducts(@RequestBody CreateProductRequest request) {
        Optional<Supplier> supplier = supplierRepository.findById(request.getSupplierId());
        if (supplier.isPresent()) {
            Product product = new Product();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setStatus(request.isStatus());
            product.setSupplier(supplier.get());
            System.out.println("Before Save" + product);
//            Supplier savedSupplier = supplierRepository.save(product);
            Product savedSupplier = productRepository.save(product);
            ProductResponse response = new ProductResponse(savedSupplier);
            response.setSupplier(supplier.map(SupplierResponse::new).get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

}
