package com.lifekhmer.health.mapper;

import com.lifekhmer.health.dto.request.MeasurementRequest;
import com.lifekhmer.health.dto.response.MeasurementResponse;
import com.lifekhmer.health.entity.HealthMeasurement;
import org.springframework.stereotype.Component;

@Component
public class HealthMeasurementMapper {

    public MeasurementResponse response(HealthMeasurement m) {
        return new MeasurementResponse(
                m.getId(),
                m.getMeasurementType(),
                m.getValue(),
                m.getSecondaryValue(),
                m.getUnit(),
                m.getRecordedAt(),
                m.getNotes(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    public void apply(HealthMeasurement m, MeasurementRequest r) {
        m.setMeasurementType(r.measurementType());
        m.setValue(r.value());
        m.setSecondaryValue(r.secondaryValue());
        m.setUnit(r.unit().trim().toLowerCase());
        m.setRecordedAt(r.recordedAt());
        m.setNotes(r.notes());
    }
}