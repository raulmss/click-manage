package com.bezkoder.spring.security.jwt.service;

import com.bezkoder.spring.inventory.mapper.AddressMapper;
import com.bezkoder.spring.security.jwt.mapper.BusinessMapper;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.payload.request.BusinessFilterDto;
import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;
import com.bezkoder.spring.security.jwt.repository.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final AddressMapper addressMapper;

    public BusinessResponseDto createBusiness(BusinessRequestDto dto) {
        if (businessRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Business name already exists.");
        }
        Business business = businessMapper.businessRequestDtoToBusiness(dto);
        return businessMapper.businessToBusinessResponseDto(businessRepository.save(business));
    }

    public BusinessResponseDto getBusinessById(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found."));
        return businessMapper.businessToBusinessResponseDto(business);
    }

    public Page<BusinessResponseDto> getAllBusinesses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return businessRepository.findAll(pageable)
                .map(businessMapper::businessToBusinessResponseDto);
    }

    public BusinessResponseDto updateBusiness(Long id, BusinessRequestDto dto) {
        Business existing = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found."));
        existing.setName(dto.name());
        existing.setIndustry(dto.industry());
        Long addressId = existing.getAddress().getId();
        existing.setAddress(addressMapper.addressRequestDtoToAddress(dto.addressRequestDto()));
        existing.getAddress().setId(addressId);
        return businessMapper.businessToBusinessResponseDto(businessRepository.save(existing));
    }

    public void deleteBusiness(Long id) {
        if (!businessRepository.existsById(id)) {
            throw new EntityNotFoundException("Business not found.");
        }
        businessRepository.deleteById(id);
    }

    public Page<BusinessResponseDto> filterBusinesses(BusinessFilterDto filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        String name = normalize(filter.name());
        String industry = normalize(filter.industry());
        String city = normalize(filter.city());
        String state = normalize(filter.state());

        return businessRepository.filterBusinesses(name, industry, city, state, pageable)
                .map(businessMapper::businessToBusinessResponseDto);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

}
