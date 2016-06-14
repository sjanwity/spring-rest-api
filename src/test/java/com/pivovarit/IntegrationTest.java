package com.pivovarit;

import com.pivovarit.domain.Shop;
import com.pivovarit.persistence.ShopsDao;
import com.pivovarit.web.dto.AddressDto;
import com.pivovarit.web.dto.ShopDto;
import com.pivovarit.web.dto.ShopWithCoordinatesDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class IntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ShopsDao shopsDao;


	private final RestTemplate restTemplate = new RestTemplate();

	@Test
    @DirtiesContext
	public void shouldAddShopAndFetchCoordinates() {
		// given
        final ShopDto newShop = new ShopDto("Test1", new AddressDto(null, "Redcross Way ", "SE1"));

        // when
        restTemplate.postForObject(baseUrl() + "/shops", newShop, Void.class);

		// then
        final Optional<Shop> addedShop = shopsDao.findAll().stream()
                .filter(shop -> Objects.equals(shop.getShopName(), newShop.getShopName()))
                .filter(shop -> Objects.equals(shop.getShopAddress().getPostCode(), newShop.getShopAddress().getPostCode()))
                .filter(shop -> Objects.equals(shop.getShopAddress().getNumber(), newShop.getShopAddress().getNumber()))
                .filter(shop -> Objects.equals(shop.getShopAddress().getStreet(), newShop.getShopAddress().getStreet()))
                .filter(shop -> Objects.nonNull(shop.getCoordinates()))
                .findAny();

        assertThat(addedShop).isPresent();
    }

	@Test
    @DirtiesContext
	public void shouldFindTheNearestShop() {
		// given
        final ShopDto newShop = new ShopDto("Test1", new AddressDto(null, "Redcross Way", "SE1"));
        restTemplate.postForObject(baseUrl() + "/shops", newShop, Void.class);

		// when
        final ShopWithCoordinatesDto nearest = restTemplate.getForObject(
                String.format("%s/nearest?customerLongitude=1&customerLatitude=1", baseUrl()),
                ShopWithCoordinatesDto.class);

        // then
        assertThat(nearest.getShopName()).isEqualTo(newShop.getShopName());
	}

    @Test
    @DirtiesContext
    public void shouldFindTheNearestShopFromBoth() {
        // given
        final ShopDto ukShop = new ShopDto("Test1", new AddressDto(null, "Redcross Way", "SE1"));
        final ShopDto canadaShop = new ShopDto("Test2", new AddressDto(null, "Leslie St", "M3C"));

        restTemplate.postForObject(baseUrl() + "/shops", ukShop, Void.class);
        restTemplate.postForObject(baseUrl() + "/shops", canadaShop, Void.class);

        // when
        final ShopWithCoordinatesDto nearest = restTemplate.getForObject(
                String.format("%s/nearest?customerLongitude=0.573453&customerLatitude=50.854259", baseUrl()),
                ShopWithCoordinatesDto.class);

        // then
        assertThat(nearest.getShopName()).isEqualTo(ukShop.getShopName());
    }

    private String baseUrl() {
        return String.format("http://localhost:%s", port);
    }

}
