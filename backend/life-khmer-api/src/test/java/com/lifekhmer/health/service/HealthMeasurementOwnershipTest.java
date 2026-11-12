package com.lifekhmer.health.service;

import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.common.exception.ResourceNotFoundException;
import com.lifekhmer.health.mapper.HealthMeasurementMapper;
import com.lifekhmer.health.repository.HealthMeasurementRepository;
import com.lifekhmer.health.service.impl.HealthMeasurementServiceImpl;
import com.lifekhmer.security.CurrentUser;
import com.lifekhmer.security.CurrentUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HealthMeasurementOwnershipTest {
    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }
    @Test void userCannotReadAnotherUsersMeasurement() {
        UUID userA = UUID.randomUUID(); UUID userBMeasurement = UUID.randomUUID();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new CurrentUser(userA), null, List.of()));
        HealthMeasurementRepository measurements = mock(HealthMeasurementRepository.class);
        when(measurements.findByIdAndUserId(userBMeasurement, userA)).thenReturn(Optional.empty());
        HealthMeasurementServiceImpl service = new HealthMeasurementServiceImpl(measurements, mock(UserRepository.class), new HealthMeasurementMapper(), new CurrentUserService());
        assertThrows(ResourceNotFoundException.class, () -> service.get(userBMeasurement));
        verify(measurements).findByIdAndUserId(userBMeasurement, userA);
    }
}
