package com.example.RKSP_7;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends R2dbcRepository<Cat, Long> {
}
