package com.lifekhmer.symptoms.service;

import com.lifekhmer.symptoms.dto.request.SymptomRecordRequest;
import com.lifekhmer.symptoms.dto.response.SymptomRecordResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SymptomService {
    SymptomRecordResponse createRecord(SymptomRecordRequest request);
    Page<SymptomRecordResponse> listRecords(int page, int size);
    SymptomRecordResponse getRecord(UUID id);
    SymptomRecordResponse updateRecord(UUID id, SymptomRecordRequest request);
    void deleteRecord(UUID id);
}
