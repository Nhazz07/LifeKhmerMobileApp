package com.lifekhmer.health.service.impl;

import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.common.exception.*;
import com.lifekhmer.health.dto.request.MeasurementRequest;
import com.lifekhmer.health.dto.response.MeasurementResponse;
import com.lifekhmer.health.entity.*;
import com.lifekhmer.health.mapper.HealthMeasurementMapper;
import com.lifekhmer.health.repository.HealthMeasurementRepository;
import com.lifekhmer.health.service.HealthMeasurementService;
import com.lifekhmer.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HealthMeasurementServiceImpl implements HealthMeasurementService {

    private final HealthMeasurementRepository repo;
    private final UserRepository users;
    private final HealthMeasurementMapper mapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public MeasurementResponse create(MeasurementRequest r) {
        validate(r);

        HealthMeasurement m = new HealthMeasurement();
        m.setUser(users.findById(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found")));
        mapper.apply(m, r);

        return mapper.response(repo.save(m));
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementResponse get(UUID id) {
        return mapper.response(owned(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> list(
            MeasurementType type,
            LocalDate from,
            LocalDate to,
            int page,
            int size
    ) {
        LocalDateTime start = (from == null ? LocalDate.of(1970, 1, 1) : from).atStartOfDay();
        LocalDateTime end = (to == null ? LocalDate.now() : to).plusDays(1).atStartOfDay();

        Pageable pageable = PageRequest.of(
                page,
                Math.min(size, 100),
                Sort.by(Sort.Direction.DESC, "recordedAt")
        );

        return (type == null
                ? repo.findByUserIdAndRecordedAtBetween(
                currentUserService.getCurrentUserId(), start, end, pageable)
                : repo.findByUserIdAndMeasurementTypeAndRecordedAtBetween(
                currentUserService.getCurrentUserId(), type, start, end, pageable))
                .map(mapper::response);
    }

    @Override
    @Transactional
    public MeasurementResponse update(UUID id, MeasurementRequest r) {
        validate(r);

        HealthMeasurement m = owned(id);
        mapper.apply(m, r);

        return mapper.response(m);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repo.delete(owned(id));
    }

    private HealthMeasurement owned(UUID id) {
        return repo.findByIdAndUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Health measurement not found"));
    }

    private void validate(MeasurementRequest r) {
        boolean bp = r.measurementType() == MeasurementType.BLOOD_PRESSURE;

        if (bp != (r.secondaryValue() != null)) {
            throw new BadRequestException("Blood pressure requires both systolic and diastolic values");
        }

        if (!bp && r.secondaryValue() != null) {
            throw new BadRequestException("A secondary value is only supported for blood pressure");
        }
    }
}