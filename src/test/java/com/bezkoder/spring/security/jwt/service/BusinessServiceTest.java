package com.bezkoder.spring.security.jwt.service;

import com.bezkoder.spring.inventory.mapper.AddressMapper;
import com.bezkoder.spring.security.jwt.mapper.BusinessMapper;
import com.bezkoder.spring.security.jwt.models.Address;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.payload.request.BusinessFilterDto;
import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;
import com.bezkoder.spring.security.jwt.repository.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessServiceTest {

//    @Mock
//    private BusinessRepository businessRepository;
//
//    @Mock
//    private BusinessMapper businessMapper;
//
//    @Mock
//    private AddressMapper addressMapper;
//
//    @InjectMocks
//    private BusinessService businessService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createBusiness_shouldCreateNewBusiness() {
//        BusinessRequestDto dto = new BusinessRequestDto("TestCorp", "IT", null, null);
//        Business business = new Business();
//        BusinessResponseDto responseDto = new BusinessResponseDto(1L, "TestCorp", "IT", null, null);
//
//        when(businessRepository.existsByName("TestCorp")).thenReturn(false);
//        when(businessMapper.businessRequestDtoToBusiness(dto)).thenReturn(business);
//        when(businessRepository.save(business)).thenReturn(business);
//        when(businessMapper.businessToBusinessResponseDto(business)).thenReturn(responseDto);
//
//        BusinessResponseDto result = businessService.createBusiness(dto);
//
//        assertEquals(responseDto, result);
//        verify(businessRepository).save(business);
//    }
//
//    @Test
//    void createBusiness_shouldThrowIfNameExists() {
//        BusinessRequestDto dto = new BusinessRequestDto("TestCorp", "IT", null, null);
//        when(businessRepository.existsByName("TestCorp")).thenReturn(true);
//
//        assertThrows(IllegalArgumentException.class, () -> businessService.createBusiness(dto));
//    }
//
//    @Test
//    void getBusinessById_shouldReturnBusiness() {
//        Business business = new Business();
//        BusinessResponseDto responseDto = new BusinessResponseDto(1L, "Test", "Tech", null, null);
//
//        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
//        when(businessMapper.businessToBusinessResponseDto(business)).thenReturn(responseDto);
//
//        BusinessResponseDto result = businessService.getBusinessById(1L);
//        assertEquals(responseDto, result);
//    }
//
//    @Test
//    void getBusinessById_shouldThrowIfNotFound() {
//        when(businessRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> businessService.getBusinessById(1L));
//    }
//
//    @Test
//    void getAllBusinesses_shouldReturnPage() {
//        Business business = new Business();
//        BusinessResponseDto responseDto = new BusinessResponseDto(1L, "Test", "Tech", null, null);
//
//        Page<Business> businessPage = new PageImpl<>(List.of(business));
//        when(businessRepository.findAll(any(Pageable.class))).thenReturn(businessPage);
//        when(businessMapper.businessToBusinessResponseDto(business)).thenReturn(responseDto);
//
//        Page<BusinessResponseDto> result = businessService.getAllBusinesses(0, 10);
//        assertEquals(1, result.getTotalElements());
//    }
//
//    @Test
//    void updateBusiness_shouldUpdateAndReturnDto() {
//        BusinessRequestDto dto = new BusinessRequestDto("NewName", "NewIndustry", null, null);
//        Address address = new Address();
//        address.setId(1L);
//        Business existing = new Business();
//        existing.setAddress(address);
//        BusinessResponseDto responseDto = new BusinessResponseDto(1L, "NewName", "NewIndustry", null, null);
//
//        when(businessRepository.findById(1L)).thenReturn(Optional.of(existing));
//        when(addressMapper.addressRequestDtoToAddress(null)).thenReturn(address);
//        when(businessRepository.save(existing)).thenReturn(existing);
//        when(businessMapper.businessToBusinessResponseDto(existing)).thenReturn(responseDto);
//
//        BusinessResponseDto result = businessService.updateBusiness(1L, dto);
//
//        assertEquals(responseDto, result);
//        verify(businessRepository).save(existing);
//    }
//
//    @Test
//    void updateBusiness_shouldThrowIfNotFound() {
//        BusinessRequestDto dto = new BusinessRequestDto("Name", "Industry", null, null);
//        when(businessRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> businessService.updateBusiness(1L, dto));
//    }
//
//    @Test
//    void deleteBusiness_shouldDeleteIfExists() {
//        when(businessRepository.existsById(1L)).thenReturn(true);
//        businessService.deleteBusiness(1L);
//        verify(businessRepository).deleteById(1L);
//    }
//
//    @Test
//    void deleteBusiness_shouldThrowIfNotExists() {
//        when(businessRepository.existsById(1L)).thenReturn(false);
//        assertThrows(EntityNotFoundException.class, () -> businessService.deleteBusiness(1L));
//    }
//
//    @Test
//    void filterBusinesses_shouldReturnFilteredPage() {
//        BusinessFilterDto filter = new BusinessFilterDto("name", "industry", "city", "state");
//        Business business = new Business();
//        BusinessResponseDto dto = new BusinessResponseDto(1L, "name", "industry", null, null);
//        Page<Business> page = new PageImpl<>(List.of(business));
//
//        when(businessRepository.filterBusinesses("name", "industry", "city", "state", PageRequest.of(0, 10)))
//                .thenReturn(page);
//        when(businessMapper.businessToBusinessResponseDto(business)).thenReturn(dto);
//
//        Page<BusinessResponseDto> result = businessService.filterBusinesses(filter, 0, 10);
//        assertEquals(1, result.getTotalElements());
//    }
//
//    @Test
//    void normalize_shouldReturnNullForBlankOrNull() {
//        assertNull(invokeNormalize(null));
//        assertNull(invokeNormalize("   "));
//        assertEquals("value", invokeNormalize("value"));
//    }
//
//    // Use reflection to invoke private normalize method for full test coverage
//    private String invokeNormalize(String input) {
//        try {
//            var method = BusinessService.class.getDeclaredMethod("normalize", String.class);
//            method.setAccessible(true);
//            return (String) method.invoke(businessService, input);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
