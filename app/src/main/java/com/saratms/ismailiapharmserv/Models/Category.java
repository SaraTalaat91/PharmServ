package com.saratms.ismailiapharmserv.Models;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class Category {

    String categoryId;
    String categoryName;
    String categoryImagePath;

    public Category(String categoryId, String categoryName, String categoryImagePath) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImagePath = categoryImagePath;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImagePath() {
        return categoryImagePath;
    }

    public void setCategoryImagePath(String categoryImagePath) {
        this.categoryImagePath = categoryImagePath;
    }
}
