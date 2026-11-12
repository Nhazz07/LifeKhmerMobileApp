package com.lifekhmer.medication.service;

import com.lifekhmer.medication.dto.request.MedicationLogRequest;
import com.lifekhmer.medication.dto.request.MedicationRequest;
import com.lifekhmer.medication.dto.response.MedicationLogResponse;
import com.lifekhmer.medication.dto.response.MedicationResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface MedicationService {
    MedicationResponse createMedication(MedicationRequest request);
    Page<MedicationResponse> listMedications(int page, int size);
    List<MedicationResponse> listActiveMedications();
    MedicationResponse getMedication(UUID id);
    MedicationResponse updateMedication(UUID id, MedicationRequest request);
    void deleteMedication(UUID id);

    MedicationLogResponse recordLog(MedicationLogRequest request);
    Page<MedicationLogResponse> listLogs(int page, int size);
}
