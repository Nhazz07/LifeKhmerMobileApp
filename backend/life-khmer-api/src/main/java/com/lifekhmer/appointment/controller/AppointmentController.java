package com.lifekhmer.appointment.controller;

import com.lifekhmer.appointment.dto.request.AppointmentRequest;
import com.lifekhmer.appointment.dto.response.AppointmentResponse;
import com.lifekhmer.appointment.service.AppointmentService;
import com.lifekhmer.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Appointment scheduled", appointmentService.createAppointment(request)));
    }

    @GetMapping
    public ApiResponse<Page<AppointmentResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Appointments retrieved", appointmentService.listAppointments(page, size));
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<AppointmentResponse>> listUpcoming() {
        return new ApiResponse<>(true, "Upcoming appointments retrieved", appointmentService.listUpcomingAppointments());
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Appointment retrieved", appointmentService.getAppointment(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequest request) {
        return new ApiResponse<>(true, "Appointment updated", appointmentService.updateAppointment(id, request));
    }

    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable UUID id) {
        appointmentService.cancelAppointment(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }
}
