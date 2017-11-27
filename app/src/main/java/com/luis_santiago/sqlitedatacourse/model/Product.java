package com.luis_santiago.sqlitedatacourse.model;

/**
 * Created by Luis Fernando Santiago Ruiz on 11/22/17.
 */

public class Product {

    private String name;
    private int price;
    private int quantity;
    private String supplierName;
    private String supplieremail;
    private int supplierNumber;

    public Product(String name, int price, int quantity, String supplierName, String supplieremail, int supplierNumber) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplieremail = supplieremail;
        this.supplierNumber = supplierNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplieremail() {
        return supplieremail;
    }

    public void setSupplieremail(String supplieremail) {
        this.supplieremail = supplieremail;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }
}
