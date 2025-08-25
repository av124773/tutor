package com.gtalent.tutor.responses;

import com.gtalent.tutor.models.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.List;

public class SupplierResponse {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private List<ProductResponse> products;

    public SupplierResponse(){}

    public SupplierResponse(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public SupplierResponse(Supplier supplier){
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.address = supplier.getAddress();
        this.phone = supplier.getPhone();
        this.email = supplier.getEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
