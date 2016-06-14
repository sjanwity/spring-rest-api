package com.pivovarit.persistence.internal;

import com.pivovarit.domain.Shop;
import com.pivovarit.persistence.ShopsDao;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Internal storage repository
 */
@Repository
public class ShopsDaoLocalImpl implements ShopsDao {

    private final Set<Shop> shops = new HashSet<>();

    @Override
    public Set<Shop> findAll() {
        return shops;
    }

    @Override
    public void save(Shop shop) {
        shops.add(shop);
    }
}
