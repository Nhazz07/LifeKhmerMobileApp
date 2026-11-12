package com.lifekhmer.appointment.service;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.appointment.dto.request.AppointmentRequest;
import com.lifekhmer.appointment.dto.response.AppointmentResponse;
import com.lifekhmer.appointment.entity.Appointment;
import com.lifekhmer.appointment.repository.AppointmentRepository;
import com.lifekhmer.appointment.service.impl.AppointmentServiceImpl;
import com.lifekhmer.security.CurrentUserService;
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
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

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
    void testCreateAppointmentSuccess() {
        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Appointment saved = new Appointment();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setDoctorName("Dr. Heng Sarin");
        saved.setHealthcareFacility("Calmette Hospital");
        saved.setAppointmentTime(LocalDateTime.now().plusDays(2));
        saved.setPurpose("Annual Health Checkup");
        saved.setStatus("SCHEDULED");
        saved.setReminderMinutesBefore(60);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        AppointmentRequest request = new AppointmentRequest(
                "Dr. Heng Sarin",
                "Calmette Hospital",
                LocalDateTime.now().plusDays(2),
                "Annual Health Checkup",
                "SCHEDULED",
                "Fasting required before test",
                60
        );

        AppointmentResponse response = appointmentService.createAppointment(request);

        assertThat(response).isNotNull();
        assertThat(response.doctorName()).isEqualTo("Dr. Heng Sarin");
        assertThat(response.healthcareFacility()).isEqualTo("Calmette Hospital");
        assertThat(response.status()).isEqualTo("SCHEDULED");
        verify(appointmentRepository).save(any(Appointment.class));
    }
}
