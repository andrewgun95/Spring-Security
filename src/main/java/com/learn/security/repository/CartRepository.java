package com.learn.security.repository;

import com.learn.security.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    void deleteByUserId(Long id);

    List<Cart> findByUserId(Long id);

}
