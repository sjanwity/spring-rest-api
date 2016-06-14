package com.pivovarit.web.dto;

import com.pivovarit.domain.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopWithCoordinatesDto {
    private String shopName;
    private AddressDto shopAddress;
    private CoordinatesDto coordinates;

    public static ShopWithCoordinatesDto from(Shop shop) {
        return new ShopWithCoordinatesDto(shop.getShopName(),
                new AddressDto(shop.getShopAddress().getNumber(), shop.getShopAddress().getStreet(), shop.getShopAddress().getPostCode()),
                new CoordinatesDto(shop.getCoordinates().getLongitude(), shop.getCoordinates().getLatitude()));
    }
}
