package com.bezkoder.spring.security.jwt.controllers;

import com.bezkoder.spring.inventory.dto.response.AddressResponseDto;
import com.bezkoder.spring.security.jwt.payload.request.BusinessFilterDto;
import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;
import com.bezkoder.spring.security.jwt.service.BusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.bezkoder.spring.security.jwt.controller.BusinessController.class)
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessService businessService;

    @Autowired
    private ObjectMapper objectMapper;

    private BusinessResponseDto sampleResponse() {
        return new BusinessResponseDto(
                1L,
                "MyBiz",
                "Tech",
                new AddressResponseDto(1L, "123 Main St", "City", "ST", "USA", "12345")
        );
    }

    private BusinessRequestDto sampleRequest() {
        return new BusinessRequestDto("MyBiz", "Tech", List.of(), null);
    }

    @Test
    void createBusiness_shouldReturnOk() throws Exception {
        Mockito.when(businessService.createBusiness(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MyBiz"));
    }

    @Test
    void createBusiness_invalidJson_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"invalid-json\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBusinessById_shouldReturnOk() throws Exception {
        Mockito.when(businessService.getBusinessById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/businesses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBusinessById_notFound_shouldReturn404() throws Exception {
        Mockito.when(businessService.getBusinessById(1L)).thenThrow(new EntityNotFoundException("Business not found"));

        mockMvc.perform(get("/api/businesses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void filterBusinesses_shouldReturnPage() throws Exception {
        Page<BusinessResponseDto> page = new PageImpl<>(List.of(sampleResponse()));

        Mockito.when(businessService.filterBusinesses(
                        ArgumentMatchers.<BusinessFilterDto>any(), eq(0), eq(10)))
                .thenReturn(page);

        mockMvc.perform(get("/api/businesses")
                        .param("name", "MyBiz")
                        .param("industry", "Tech")
                        .param("city", "City")
                        .param("state", "ST")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("MyBiz"));
    }

    @Test
    void updateBusiness_shouldReturnOk() throws Exception {
        BusinessRequestDto request = new BusinessRequestDto("Updated", "Updated", List.of(), null);

        AddressResponseDto addressResponseDto = new AddressResponseDto(
                1L, "123 Main St", "City", "ST", "USA", "12345"
        );

        BusinessResponseDto response = new BusinessResponseDto(
                1L, "Updated", "Updated", addressResponseDto
        );

        Mockito.when(businessService.updateBusiness(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/businesses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.addressResponseDto.street").value("123 Main St"));
    }

    @Test
    void updateBusiness_notFound_shouldReturn404() throws Exception {
        Mockito.when(businessService.updateBusiness(eq(1L), any()))
                .thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(put("/api/businesses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBusiness_invalidJson_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/businesses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"invalid-json\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBusiness_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/businesses/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(businessService).deleteBusiness(1L);
    }

    @Test
    void deleteBusiness_notFound_shouldReturn404() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Not found"))
                .when(businessService).deleteBusiness(1L);

        mockMvc.perform(delete("/api/businesses/1"))
                .andExpect(status().isNotFound());
    }
}