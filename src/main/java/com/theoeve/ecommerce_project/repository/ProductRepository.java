package com.theoeve.ecommerce_project.repository;

import com.theoeve.ecommerce_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByUserId(int userId);
}
