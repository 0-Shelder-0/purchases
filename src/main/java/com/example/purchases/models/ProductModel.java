package com.example.purchases.models;

import com.example.purchases.dbService.entities.Product;

public class ProductModel {

    private final int _productId;
    private final boolean _isCompleted;
    private final String _description;

    public ProductModel(Product product) {
        _productId = product.getId();
        _isCompleted = product.isCompleted();
        _description = product.getDescription();
    }

    public ProductModel(int productId, String description, boolean isCompleted) {
        _productId = productId;
        _description = description;
        _isCompleted = isCompleted;
    }

    public int getProductId() {
        return _productId;
    }

    public boolean isCompleted() {
        return _isCompleted;
    }

    public String getDescription() {
        return _description;
    }
}
