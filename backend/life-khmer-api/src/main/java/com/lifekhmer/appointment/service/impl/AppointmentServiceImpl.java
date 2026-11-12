package com.lifekhmer.appointment.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.appointment.dto.request.AppointmentRequest;
import com.lifekhmer.appointment.dto.response.AppointmentResponse;
import com.lifekhmer.appointment.entity.Appointment;
import com.lifekhmer.appointment.repository.AppointmentRepository;
import com.lifekhmer.appointment.service.AppointmentService;
import com.lifekhmer.common.exception.ResourceNotFoundException;
import com.lifekhmer.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        User user = getCurrentUser();

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDoctorName(request.doctorName().trim());
        appointment.setHealthcareFacility(request.healthcareFacility().trim());
        appointment.setAppointmentTime(request.appointmentTime());
        appointment.setPurpose(request.purpose().trim());
        appointment.setStatus(request.status() != null && !request.status().isBlank() ? request.status() : "SCHEDULED");
        appointment.setNotes(request.notes());
        if (request.reminderMinutesBefore() != null) {
            appointment.setReminderMinutesBefore(request.reminderMinutesBefore());
        }

        return mapAppointment(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> listAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "appointmentTime"));
        return appointmentRepository.findByUserId(currentUserService.getCurrentUserId(), pageable)
                .map(this::mapAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> listUpcomingAppointments() {
        return appointmentRepository.findByUserIdAndAppointmentTimeAfterAndStatusOrderByAppointmentTimeAsc(
                currentUserService.getCurrentUserId(),
                LocalDateTime.now(),
                "SCHEDULED"
        ).stream().map(this::mapAppointment).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointment(UUID id) {
        return mapAppointment(getOwnedAppointment(id));
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointment(UUID id, AppointmentRequest request) {
        Appointment appointment = getOwnedAppointment(id);
        appointment.setDoctorName(request.doctorName().trim());
        appointment.setHealthcareFacility(request.healthcareFacility().trim());
        appointment.setAppointmentTime(request.appointmentTime());
        appointment.setPurpose(request.purpose().trim());
        if (request.status() != null && !request.status().isBlank()) {
            appointment.setStatus(request.status());
        }
        appointment.setNotes(request.notes());
        if (request.reminderMinutesBefore() != null) {
            appointment.setReminderMinutesBefore(request.reminderMinutesBefore());
        }

        return mapAppointment(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void cancelAppointment(UUID id) {
        Appointment appointment = getOwnedAppointment(id);
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {
        Appointment appointment = getOwnedAppointment(id);
        appointmentRepository.delete(appointment);
    }

    private Appointment getOwnedAppointment(UUID id) {
        return appointmentRepository.findByIdAndUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    private User getCurrentUser() {
        return userRepository.findById(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private AppointmentResponse mapAppointment(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getDoctorName(),
                a.getHealthcareFacility(),
                a.getAppointmentTime(),
                a.getPurpose(),
                a.getStatus(),
                a.getNotes(),
                a.getReminderMinutesBefore(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}
