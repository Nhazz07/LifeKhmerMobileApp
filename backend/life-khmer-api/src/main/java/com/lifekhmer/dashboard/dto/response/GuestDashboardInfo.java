package com.lifekhmer.dashboard.dto.response;

import java.util.List;

public record GuestDashboardInfo(
        String appName,
        String tagline,
        List<String> features,
        String callToAction
) {}
