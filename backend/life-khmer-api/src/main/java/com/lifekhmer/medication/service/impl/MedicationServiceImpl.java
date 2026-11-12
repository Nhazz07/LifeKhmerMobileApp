package com.lifekhmer.medication.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.common.exception.ResourceNotFoundException;
import com.lifekhmer.medication.dto.request.MedicationLogRequest;
import com.lifekhmer.medication.dto.request.MedicationRequest;
import com.lifekhmer.medication.dto.response.MedicationLogResponse;
import com.lifekhmer.medication.dto.response.MedicationResponse;
import com.lifekhmer.medication.entity.Medication;
import com.lifekhmer.medication.entity.MedicationLog;
import com.lifekhmer.medication.repository.MedicationLogRepository;
import com.lifekhmer.medication.repository.MedicationRepository;
import com.lifekhmer.medication.service.MedicationService;
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
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationLogRepository medicationLogRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public MedicationResponse createMedication(MedicationRequest request) {
        User user = getCurrentUser();

        Medication medication = new Medication();
        medication.setUser(user);
        medication.setName(request.name().trim());
        medication.setDosage(request.dosage().trim());
        medication.setFrequency(request.frequency().trim());
        medication.setInstructions(request.instructions() != null ? request.instructions().trim() : null);
        medication.setStartDate(request.startDate());
        medication.setEndDate(request.endDate());
        medication.setActive(request.active() != null ? request.active() : true);

        return mapMedication(medicationRepository.save(medication));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationResponse> listMedications(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        return medicationRepository.findByUserId(currentUserService.getCurrentUserId(), pageable)
                .map(this::mapMedication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> listActiveMedications() {
        return medicationRepository.findByUserIdAndActiveTrue(currentUserService.getCurrentUserId())
                .stream()
                .map(this::mapMedication)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicationResponse getMedication(UUID id) {
        Medication medication = getOwnedMedication(id);
        return mapMedication(medication);
    }

    @Override
    @Transactional
    public MedicationResponse updateMedication(UUID id, MedicationRequest request) {
        Medication medication = getOwnedMedication(id);
        medication.setName(request.name().trim());
        medication.setDosage(request.dosage().trim());
        medication.setFrequency(request.frequency().trim());
        medication.setInstructions(request.instructions() != null ? request.instructions().trim() : null);
        medication.setStartDate(request.startDate());
        medication.setEndDate(request.endDate());
        if (request.active() != null) {
            medication.setActive(request.active());
        }

        return mapMedication(medicationRepository.save(medication));
    }

    @Override
    @Transactional
    public void deleteMedication(UUID id) {
        Medication medication = getOwnedMedication(id);
        medicationRepository.delete(medication);
    }

    @Override
    @Transactional
    public MedicationLogResponse recordLog(MedicationLogRequest request) {
        User user = getCurrentUser();
        Medication medication = getOwnedMedication(request.medicationId());

        MedicationLog log = new MedicationLog();
        log.setUser(user);
        log.setMedication(medication);
        log.setScheduledTime(request.scheduledTime());
        log.setTakenAt(request.takenAt() != null ? request.takenAt() : LocalDateTime.now());
        log.setStatus(request.status() != null && !request.status().isBlank() ? request.status() : "TAKEN");
        log.setNotes(request.notes());

        return mapLog(medicationLogRepository.save(log));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationLogResponse> listLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "scheduledTime"));
        return medicationLogRepository.findByUserId(currentUserService.getCurrentUserId(), pageable)
                .map(this::mapLog);
    }

    private Medication getOwnedMedication(UUID id) {
        return medicationRepository.findByIdAndUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
    }

    private User getCurrentUser() {
        return userRepository.findById(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private MedicationResponse mapMedication(Medication m) {
        return new MedicationResponse(
                m.getId(),
                m.getName(),
                m.getDosage(),
                m.getFrequency(),
                m.getInstructions(),
                m.getStartDate(),
                m.getEndDate(),
                m.isActive(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    private MedicationLogResponse mapLog(MedicationLog l) {
        return new MedicationLogResponse(
                l.getId(),
                l.getMedication().getId(),
                l.getMedication().getName(),
                l.getScheduledTime(),
                l.getTakenAt(),
                l.getStatus(),
                l.getNotes(),
                l.getCreatedAt()
        );
    }
}
