package com.pivovarit.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.pivovarit.domain.Address;
import com.pivovarit.domain.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.google.appengine.repackaged.com.google.common.base.Strings.nullToEmpty;

@Service
@Slf4j
public class GoogleMapsGeocodingService {

    private final GeoApiContext context;

    @Autowired
    public GoogleMapsGeocodingService(@Value("${api.key}") String apiKey) {
        this.context = new GeoApiContext()
                .setApiKey(apiKey);
    }

    public Optional<Coordinates> fetchCoordinatesFor(Address address) {
        Objects.requireNonNull(address);

        log.debug("Fetching coordinates for: " + encode(address));

        final GeocodingResult[] result;

        try {
            result = GeocodingApi.geocode(context, encode(address)).await();
        } catch (Exception e) {
            return Optional.empty();
        }

        return Arrays.stream(result)
                .findFirst()
                .map(r -> r.geometry.location)
                .map(location -> new Coordinates(location.lng, location.lat));
    }

    private static String encode(Address address) {
        return String.format("%s %s %s", nullToEmpty(address.getStreet()),
                address.getNumber() == null ? "" : address.getNumber().toString(),
                nullToEmpty(address.getPostCode()));
    }
}
