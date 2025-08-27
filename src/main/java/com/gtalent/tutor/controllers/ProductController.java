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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@Tag(name = "產品", description = "產品控制器，提供產品資訊新增、刪除、更新、查詢功能。")
public class ProductController {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, SupplierRepository supplierRepository){
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @Operation(summary = "取得所有產品", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products.stream().map(product -> {
            ProductResponse response = new ProductResponse(product);
            response.setSupplier(new SupplierResponse(product.getSupplier()));
            return response;
        }).toList());
    }

    @Operation(summary = "取得指定產品", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
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

    @Operation(summary = "跟新指定產品", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
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

    @Operation(summary = "創造新產品", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
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

    @Operation(summary = "刪除指定產品", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
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
