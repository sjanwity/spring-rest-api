package com.pivovarit.domain.exception;

import com.pivovarit.domain.Address;

public class CouldNotDetermineLocationException extends RuntimeException {
    public CouldNotDetermineLocationException(Address address) {
        super(String.format("Location: [%s %d %s]", address.getStreet(), address.getNumber(), address.getPostCode()));
    }
}
