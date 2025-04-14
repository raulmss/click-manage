package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.AddressRequestDto;
import com.bezkoder.spring.inventory.dto.response.AddressResponseDto;
import com.bezkoder.spring.inventory.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address addressRequestDtoToAddress(AddressRequestDto dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setCountry(dto.country());
        address.setPostalCode(dto.postalCode());
        return address;
    }

    public AddressResponseDto addressToAddressResponseDto(Address address) {
        if (address == null) return null;

        return new AddressResponseDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );
    }
}
