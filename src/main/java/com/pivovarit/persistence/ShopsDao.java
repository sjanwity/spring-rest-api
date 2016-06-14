package com.pivovarit.persistence;

import com.pivovarit.domain.Shop;

import java.util.Set;

public interface ShopsDao {
    Set<Shop> findAll();
    void save(Shop shop);
}
