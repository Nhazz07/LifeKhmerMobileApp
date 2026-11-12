package com.lifekhmer.medication.service;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.medication.dto.request.MedicationRequest;
import com.lifekhmer.medication.dto.response.MedicationResponse;
import com.lifekhmer.medication.entity.Medication;
import com.lifekhmer.medication.repository.MedicationLogRepository;
import com.lifekhmer.medication.repository.MedicationRepository;
import com.lifekhmer.medication.service.impl.MedicationServiceImpl;
import com.lifekhmer.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;
    @Mock
    private MedicationLogRepository medicationLogRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private MedicationServiceImpl medicationService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setFullName("Chann Sok");
    }

    @Test
    void testCreateMedicationSuccess() {
        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Medication saved = new Medication();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setName("Amoxicillin");
        saved.setDosage("500mg");
        saved.setFrequency("3 times daily");
        saved.setInstructions("Take after meals");
        saved.setStartDate(LocalDate.now());
        saved.setActive(true);

        when(medicationRepository.save(any(Medication.class))).thenReturn(saved);

        MedicationRequest request = new MedicationRequest(
                "Amoxicillin",
                "500mg",
                "3 times daily",
                "Take after meals",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                true
        );

        MedicationResponse response = medicationService.createMedication(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Amoxicillin");
        assertThat(response.dosage()).isEqualTo("500mg");
        assertThat(response.active()).isTrue();
        verify(medicationRepository).save(any(Medication.class));
    }
}
