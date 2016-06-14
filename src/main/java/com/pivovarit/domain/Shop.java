package com.pivovarit.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Basic entity representing a shop
 */
@Data
@Builder
public class Shop {
    private final String shopName;
    private final Address shopAddress;
    private final Coordinates coordinates;
}
