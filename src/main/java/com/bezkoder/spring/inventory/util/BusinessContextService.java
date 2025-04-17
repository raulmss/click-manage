package com.bezkoder.spring.inventory.util;

import com.bezkoder.spring.security.jwt.exception.BusinessNotFoundException;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessContextService {

    private final UserRepository userRepository;

    public Business getCurrentBusiness() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Business business = user.getBusiness();
        if (business == null) {
            throw new BusinessNotFoundException(user.getUsername());
        }

        return business;
    }
}
