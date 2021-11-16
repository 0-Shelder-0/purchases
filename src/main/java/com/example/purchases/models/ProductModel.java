package com.example.purchases.models;

import com.example.purchases.dbService.entities.Product;

public class ProductModel {

    private final int _userId;
    private final String _description;

    public ProductModel(Product product) {
        _userId = product.getUser().getId();
        _description = product.getDescription();
    }

    public int getUserId() {
        return _userId;
    }

    public String getDescription() {
        return _description;
    }
}
