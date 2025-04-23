package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.SupplierRequestDto;
import com.bezkoder.spring.inventory.dto.response.SupplierResponseDto;
import com.bezkoder.spring.inventory.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    private final AddressMapper addressMapper;

    @Autowired
    public SupplierMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Supplier supplierRequestDtoToSupplier(SupplierRequestDto dto) {
        if (dto == null) return null;

        Supplier supplier = new Supplier();
        supplier.setName(dto.name());
        supplier.setEmail(dto.email());
        supplier.setPhone(dto.phone());
        supplier.setTaxId(dto.taxId());
        supplier.setContactPerson(dto.contactPerson());
        supplier.setAddress(addressMapper.addressRequestDtoToAddress(dto.addressRequestDto()));
        return supplier;
    }

    public SupplierResponseDto supplierToSupplierResponseDto(Supplier supplier) {
        if (supplier == null) return null;

        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getTaxId(),
                addressMapper.addressToAddressResponseDto(supplier.getAddress()),
                supplier.getContactPerson()
        );
    }
}
