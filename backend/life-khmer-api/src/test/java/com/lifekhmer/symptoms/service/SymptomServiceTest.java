package com.lifekhmer.symptoms.service;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.security.CurrentUserService;
import com.lifekhmer.symptoms.dto.request.SymptomRecordRequest;
import com.lifekhmer.symptoms.dto.response.SymptomRecordResponse;
import com.lifekhmer.symptoms.entity.SymptomRecord;
import com.lifekhmer.symptoms.repository.SymptomRecordRepository;
import com.lifekhmer.symptoms.service.impl.SymptomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SymptomServiceTest {

    @Mock
    private SymptomRecordRepository symptomRecordRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private SymptomServiceImpl symptomService;

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
    void testCreateSymptomRecordSuccess() {
        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        SymptomRecord saved = new SymptomRecord();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setSymptomName("Mild Headache");
        saved.setSeverity(3);
        saved.setNotes("Frontal temple ache after screen time");
        saved.setRecordedAt(LocalDateTime.now());

        when(symptomRecordRepository.save(any(SymptomRecord.class))).thenReturn(saved);

        SymptomRecordRequest request = new SymptomRecordRequest(
                "Mild Headache",
                3,
                "Frontal temple ache after screen time",
                LocalDateTime.now()
        );

        SymptomRecordResponse response = symptomService.createRecord(request);

        assertThat(response).isNotNull();
        assertThat(response.symptomName()).isEqualTo("Mild Headache");
        assertThat(response.severity()).isEqualTo(3);
        verify(symptomRecordRepository).save(any(SymptomRecord.class));
    }
}
