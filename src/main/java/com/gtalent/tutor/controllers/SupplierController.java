package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.Supplier;
import com.gtalent.tutor.repositories.SupplierRepository;
import com.gtalent.tutor.requests.CreateSupplierRequest;
import com.gtalent.tutor.requests.UpdateSupplierRequest;
import com.gtalent.tutor.responses.ProductResponse;
import com.gtalent.tutor.responses.SupplierResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository =supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
//        return ResponseEntity.ok(suppliers.stream().map(SupplierResponse::new).toList());
        return ResponseEntity.ok(suppliers.stream().map(supplier -> {
            SupplierResponse response = new SupplierResponse(supplier);
            response.setProducts(supplier.getProducts().stream().map(ProductResponse::new).toList());
            return response;
        }).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable int id ) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            SupplierResponse response = new SupplierResponse(supplier.get());
            response.setProducts(supplier.get().getProducts().stream().map(ProductResponse::new).toList());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplierById(@PathVariable int id, @RequestBody UpdateSupplierRequest request) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            Supplier updatedSupplier = supplier.get();
            updatedSupplier.setName(request.getName());
            updatedSupplier.setPhone(request.getPhone());
            Supplier saveSupplier = supplierRepository.save(updatedSupplier);
            SupplierResponse response = new SupplierResponse(saveSupplier);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<SupplierResponse> createSuppliers(@RequestBody CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setEmail(request.getEmail());
        System.out.println("Before Save" + supplier);
        Supplier savedUser = supplierRepository.save(supplier);
        SupplierResponse response = new SupplierResponse(savedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable int id) {

        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            supplierRepository.delete(supplier.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }
}
