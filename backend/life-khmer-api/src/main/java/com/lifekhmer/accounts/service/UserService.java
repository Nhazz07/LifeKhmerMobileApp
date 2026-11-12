package com.lifekhmer.accounts.service;
import com.lifekhmer.accounts.dto.request.UpdateProfileRequest;
import com.lifekhmer.accounts.dto.response.ProfileResponse;

public interface UserService {
    ProfileResponse getCurrentProfile();
    ProfileResponse updateCurrentProfile(UpdateProfileRequest request);
}
