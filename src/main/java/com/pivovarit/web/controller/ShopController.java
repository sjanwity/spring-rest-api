package com.pivovarit.web.controller;

import com.pivovarit.domain.Address;
import com.pivovarit.domain.Coordinates;
import com.pivovarit.domain.Shop;
import com.pivovarit.domain.exception.CouldNotDetermineLocationException;
import com.pivovarit.persistence.ShopsDao;
import com.pivovarit.service.GoogleMapsGeocodingService;
import com.pivovarit.web.dto.ShopDto;
import com.pivovarit.web.dto.ShopWithCoordinatesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;

import static com.pivovarit.domain.Address.from;
import static com.pivovarit.utils.CoordinatesUtils.distanceBetween;
import static java.util.stream.Collectors.toSet;

@RestController
@Slf4j
public class ShopController {

    private final GoogleMapsGeocodingService mapsGeocodingService;

    private final ShopsDao shopsDao;

    @Autowired
    public ShopController(GoogleMapsGeocodingService mapsGeocodingService, ShopsDao shopsDao) {
        this.mapsGeocodingService = mapsGeocodingService;
        this.shopsDao = shopsDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String foo() {
        return "SPRING-BOOT-DEMO";
    }

    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    @ResponseBody
    public Optional<ShopWithCoordinatesDto> getNearest(@RequestParam double customerLongitude, @RequestParam double customerLatitude) {
        return shopsDao.findAll().stream()
                .reduce(byDistanceTo(customerLongitude, customerLatitude))
                .map(ShopWithCoordinatesDto::from);
    }

    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    @ResponseBody
    public Set<ShopWithCoordinatesDto> findAll() {
        return shopsDao.findAll().stream()
                .map(ShopWithCoordinatesDto::from)
                .collect(toSet());
    }

    @RequestMapping(value = "/shops", method = RequestMethod.POST)
    @ResponseBody
    public void save(@RequestBody ShopDto shop) {

        final Address address = from(shop.getShopAddress());

        final Coordinates coordinates = mapsGeocodingService.fetchCoordinatesFor(address)
                .orElseThrow(() -> new CouldNotDetermineLocationException(address));

        final Shop newShop = Shop.builder()
                .shopName(shop.getShopName())
                .shopAddress(address)
                .coordinates(coordinates)
                .build();

        shopsDao.save(newShop);
    }

    private static BinaryOperator<Shop> byDistanceTo(double customerLongitude, double customerLatitude) {
        return (s1, s2) -> {
            final double s1Distance = distanceBetween(s1.getCoordinates(), new Coordinates(customerLongitude, customerLatitude));
            final double s2Distance = distanceBetween(s2.getCoordinates(), new Coordinates(customerLongitude, customerLatitude));

            return s1Distance < s2Distance ? s1 : s2;
        };
    }
}
