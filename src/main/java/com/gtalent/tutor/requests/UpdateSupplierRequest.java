package com.gtalent.tutor.requests;

public class UpdateSupplierRequest {
    private String name;
    private String phone;

    public UpdateSupplierRequest() {
    }

    public UpdateSupplierRequest(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
