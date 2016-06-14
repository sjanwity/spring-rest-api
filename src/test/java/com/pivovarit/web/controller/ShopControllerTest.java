package com.pivovarit.web.controller;

import com.pivovarit.domain.Coordinates;
import com.pivovarit.persistence.internal.ShopsDaoLocalImpl;
import com.pivovarit.service.GoogleMapsGeocodingService;
import com.pivovarit.web.dto.AddressDto;
import com.pivovarit.web.dto.CoordinatesDto;
import com.pivovarit.web.dto.ShopDto;
import com.pivovarit.web.dto.ShopWithCoordinatesDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ShopControllerTest {

    private static final Coordinates COORDINATES = new Coordinates(1, 1);

    private static ShopController shopController;

    @Before
    public void setup() throws Exception {
        final GoogleMapsGeocodingService service = Mockito.mock(GoogleMapsGeocodingService.class);
        Mockito.when(service.fetchCoordinatesFor(any())).thenReturn(Optional.of(COORDINATES));

        shopController = new ShopController(service, new ShopsDaoLocalImpl());
    }

    @Test
    @DirtiesContext
    public void shouldGetNearestShop() {
        // given
        final ShopDto givenDto = new ShopDto("TEST", new AddressDto(1, "S", "0000"));

        // when
        shopController.save(givenDto);

        // It's ok to use Optional.get() for testing
        final ShopWithCoordinatesDto result = shopController.getNearest(COORDINATES.getLongitude(), COORDINATES.getLatitude()).get();

        // then
        assertThat(result.getShopName()).isEqualTo(givenDto.getShopName());
        assertThat(result.getShopAddress()).isEqualTo(givenDto.getShopAddress());
        assertThat(result.getCoordinates().getLatitude()).isEqualTo(COORDINATES.getLatitude());
        assertThat(result.getCoordinates().getLongitude()).isEqualTo(COORDINATES.getLongitude());
    }

    @Test
    public void shouldNotFindNearest() {
        // when
        final Optional<ShopWithCoordinatesDto> result = shopController.getNearest(1, 1);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnZeroShops() {
        // when
        final Set<ShopWithCoordinatesDto> result = shopController.findAll().stream()
                .collect(toSet());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DirtiesContext
    public void shouldAddShop() {
        // given
        final ShopDto givenDto = new ShopDto("TEST", new AddressDto(1, "S", "0000"));

        final ShopWithCoordinatesDto expected = new ShopWithCoordinatesDto(givenDto.getShopName(),
                givenDto.getShopAddress(), new CoordinatesDto(COORDINATES.getLongitude(), COORDINATES.getLatitude()));

        // when
        shopController.save(givenDto);

        final Set<ShopWithCoordinatesDto> result = shopController.findAll().stream().collect(toSet());

        // then
        assertThat(result)
                .hasSize(1)
                .contains(expected);
    }

}