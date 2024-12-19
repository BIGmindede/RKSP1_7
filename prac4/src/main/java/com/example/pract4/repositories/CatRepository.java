package com.example.pract4.repositories;

import com.example.pract4.models.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {
    Cat findCatById(Long id);
}
