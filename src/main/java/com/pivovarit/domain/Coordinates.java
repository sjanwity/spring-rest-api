package com.pivovarit.domain;

import lombok.Value;

/**
 * Value object representing a set of coordinates
 */
@Value
public class Coordinates {
    private final double longitude;
    private final double latitude;
}
