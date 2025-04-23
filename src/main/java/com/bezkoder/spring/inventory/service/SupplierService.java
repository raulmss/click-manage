package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.SupplierRequestDto;
import com.bezkoder.spring.inventory.dto.response.SupplierResponseDto;
import com.bezkoder.spring.inventory.exception.SupplierAlreadyExistsException;
import com.bezkoder.spring.inventory.mapper.AddressMapper;
import com.bezkoder.spring.inventory.mapper.SupplierMapper;
import com.bezkoder.spring.inventory.model.Supplier;
import com.bezkoder.spring.inventory.repository.SupplierRepository;
import com.bezkoder.spring.security.jwt.models.Address;
import com.bezkoder.spring.security.jwt.models.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final AddressMapper addressMapper;


    public SupplierResponseDto create(Business business, SupplierRequestDto dto) {
        if (supplierRepository.existsByNameIgnoreCaseAndBusiness(dto.name(), business)) {
            throw new SupplierAlreadyExistsException("A supplier with this name already exists for this business.");
        }

        if (dto.taxId() != null &&
                supplierRepository.existsByTaxIdAndBusiness(dto.taxId(), business)) {
            throw new SupplierAlreadyExistsException("A supplier with this tax ID already exists for this business.");
        }

        Supplier supplier = supplierMapper.supplierRequestDtoToSupplier(dto);
        supplier.setBusiness(business);

        // Persist address before setting
        Address address = addressMapper.addressRequestDtoToAddress(dto.addressRequestDto());
        supplier.setAddress(address);

        return supplierMapper.supplierToSupplierResponseDto(supplierRepository.save(supplier));
    }

    public Page<SupplierResponseDto> findAll(Business business, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return supplierRepository.filterSuppliers(business, name, pageable)
                .map(supplierMapper::supplierToSupplierResponseDto);
    }

    public Optional<Supplier> findByIdAndBusiness(Long id, Business business) {
        return supplierRepository.findByIdAndBusiness(id, business);
    }

    public SupplierResponseDto update(Long id, Business business, SupplierRequestDto dto) {
        Supplier existing = supplierRepository.findByIdAndBusiness(id, business)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found."));

        if (!existing.getName().equalsIgnoreCase(dto.name()) &&
                supplierRepository.existsByNameIgnoreCaseAndBusiness(dto.name(), business)) {
            throw new SupplierAlreadyExistsException("A supplier with this name already exists for this business.");
        }

        if (dto.taxId() != null &&
                !dto.taxId().equals(existing.getTaxId()) &&
                supplierRepository.existsByTaxIdAndBusiness(dto.taxId(), business)) {
            throw new SupplierAlreadyExistsException("A supplier with this tax ID already exists for this business.");
        }

        existing.setName(dto.name());
        existing.setEmail(dto.email());
        existing.setPhone(dto.phone());
        existing.setTaxId(dto.taxId());
        existing.setContactPerson(dto.contactPerson());

        Address updatedAddress = addressMapper.addressRequestDtoToAddress(dto.addressRequestDto());

        // Save and set updated address
        existing.setAddress(updatedAddress);

        return supplierMapper.supplierToSupplierResponseDto(supplierRepository.save(existing));
    }

    public void delete(Long id, Business business) {
        Supplier supplier = supplierRepository.findByIdAndBusiness(id, business)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found."));

        supplierRepository.delete(supplier);
    }
}

