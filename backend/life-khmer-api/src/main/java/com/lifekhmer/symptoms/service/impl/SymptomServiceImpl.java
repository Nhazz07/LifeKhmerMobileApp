package com.lifekhmer.symptoms.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.common.exception.ResourceNotFoundException;
import com.lifekhmer.security.CurrentUserService;
import com.lifekhmer.symptoms.dto.request.SymptomRecordRequest;
import com.lifekhmer.symptoms.dto.response.SymptomRecordResponse;
import com.lifekhmer.symptoms.entity.SymptomRecord;
import com.lifekhmer.symptoms.repository.SymptomRecordRepository;
import com.lifekhmer.symptoms.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {

    private final SymptomRecordRepository symptomRecordRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public SymptomRecordResponse createRecord(SymptomRecordRequest request) {
        User user = getCurrentUser();

        SymptomRecord record = new SymptomRecord();
        record.setUser(user);
        record.setSymptomName(request.symptomName().trim());
        record.setSeverity(request.severity());
        record.setNotes(request.notes());
        record.setRecordedAt(request.recordedAt());

        return mapRecord(symptomRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SymptomRecordResponse> listRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "recordedAt"));
        return symptomRecordRepository.findByUserId(currentUserService.getCurrentUserId(), pageable)
                .map(this::mapRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public SymptomRecordResponse getRecord(UUID id) {
        return mapRecord(getOwnedRecord(id));
    }

    @Override
    @Transactional
    public SymptomRecordResponse updateRecord(UUID id, SymptomRecordRequest request) {
        SymptomRecord record = getOwnedRecord(id);
        record.setSymptomName(request.symptomName().trim());
        record.setSeverity(request.severity());
        record.setNotes(request.notes());
        record.setRecordedAt(request.recordedAt());

        return mapRecord(symptomRecordRepository.save(record));
    }

    @Override
    @Transactional
    public void deleteRecord(UUID id) {
        SymptomRecord record = getOwnedRecord(id);
        symptomRecordRepository.delete(record);
    }

    private SymptomRecord getOwnedRecord(UUID id) {
        return symptomRecordRepository.findByIdAndUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Symptom record not found"));
    }

    private User getCurrentUser() {
        return userRepository.findById(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private SymptomRecordResponse mapRecord(SymptomRecord r) {
        return new SymptomRecordResponse(
                r.getId(),
                r.getSymptomName(),
                r.getSeverity(),
                r.getNotes(),
                r.getRecordedAt(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
