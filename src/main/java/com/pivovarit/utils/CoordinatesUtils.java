package com.pivovarit.utils;

import com.pivovarit.domain.Coordinates;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CoordinatesUtils {

    public static double distanceBetween(Coordinates c1, Coordinates c2) {
        return Math.hypot(c1.getLatitude() - c2.getLatitude(), c1.getLongitude() - c1.getLongitude());
    }
}
