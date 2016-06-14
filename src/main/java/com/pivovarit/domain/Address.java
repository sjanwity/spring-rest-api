package com.pivovarit.domain;

import com.pivovarit.web.dto.AddressDto;
import lombok.Value;

/**
 * Value object representing an address
 */
@Value
public class Address {
    private final Integer number;
    private final String street;
    private final String postCode;

    public static Address from(AddressDto dto) {
        return new Address(dto.getNumber(), dto.getStreet(), dto.getPostCode());
    }
}
