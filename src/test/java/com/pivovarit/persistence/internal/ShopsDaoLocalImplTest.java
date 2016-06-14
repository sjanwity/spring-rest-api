package com.pivovarit.persistence.internal;

import com.pivovarit.domain.Address;
import com.pivovarit.domain.Coordinates;
import com.pivovarit.domain.Shop;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopsDaoLocalImplTest {

    @Test
    public void shouldAddToStorage() {

        // given
        final Shop givenShop = Shop.builder()
                .shopName("TestStore")
                .shopAddress(new Address(1, "S", "0000"))
                .coordinates(new Coordinates(1, 1)).build();

        final ShopsDaoLocalImpl givenDao = new ShopsDaoLocalImpl();

        // when
        givenDao.save(givenShop);

        // then
        assertThat(givenDao.findAll()).contains(givenShop);
    }

    @Test
    public void shouldContainZeroShops() {
        // given

        final ShopsDaoLocalImpl givenDao = new ShopsDaoLocalImpl();

        // when
        final Set<Shop> results = givenDao.findAll();

        // then
        assertThat(results).isEmpty();
    }

}