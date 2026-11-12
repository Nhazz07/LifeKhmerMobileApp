package com.lifekhmer.symptoms.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.symptoms.dto.request.SymptomRecordRequest;
import com.lifekhmer.symptoms.dto.response.SymptomRecordResponse;
import com.lifekhmer.symptoms.service.SymptomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/symptoms")
@RequiredArgsConstructor
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    public ResponseEntity<ApiResponse<SymptomRecordResponse>> create(@Valid @RequestBody SymptomRecordRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Symptom recorded", symptomService.createRecord(request)));
    }

    @GetMapping
    public ApiResponse<Page<SymptomRecordResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Symptoms retrieved", symptomService.listRecords(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<SymptomRecordResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Symptom details retrieved", symptomService.getRecord(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<SymptomRecordResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody SymptomRecordRequest request) {
        return new ApiResponse<>(true, "Symptom record updated", symptomService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        symptomService.deleteRecord(id);
    }
}
