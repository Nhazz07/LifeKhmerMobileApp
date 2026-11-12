package com.lifekhmer.health.service;

import com.lifekhmer.health.dto.request.MeasurementRequest;
import com.lifekhmer.health.dto.response.MeasurementResponse;
import com.lifekhmer.health.entity.MeasurementType;
import org.springframework.data.domain.Page;

import java.time.*;
import java.util.UUID;

public interface HealthMeasurementService {

    MeasurementResponse create(MeasurementRequest r);

    MeasurementResponse get(UUID id);

    Page<MeasurementResponse> list(
            MeasurementType type,
            LocalDate from,
            LocalDate to,
            int page,
            int size
    );

    MeasurementResponse update(UUID id, MeasurementRequest r);

    void delete(UUID id);
}