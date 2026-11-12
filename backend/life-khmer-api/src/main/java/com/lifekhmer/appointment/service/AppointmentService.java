package com.lifekhmer.appointment.service;

import com.lifekhmer.appointment.dto.request.AppointmentRequest;
import com.lifekhmer.appointment.dto.response.AppointmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRequest request);
    Page<AppointmentResponse> listAppointments(int page, int size);
    List<AppointmentResponse> listUpcomingAppointments();
    AppointmentResponse getAppointment(UUID id);
    AppointmentResponse updateAppointment(UUID id, AppointmentRequest request);
    void cancelAppointment(UUID id);
    void deleteAppointment(UUID id);
}
