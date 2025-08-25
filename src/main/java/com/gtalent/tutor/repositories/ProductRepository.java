package com.gtalent.tutor.repositories;

import com.gtalent.tutor.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
