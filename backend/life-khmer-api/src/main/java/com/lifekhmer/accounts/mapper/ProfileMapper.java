package com.lifekhmer.accounts.mapper;
import com.lifekhmer.accounts.dto.request.UpdateProfileRequest; import com.lifekhmer.accounts.dto.response.ProfileResponse; import com.lifekhmer.accounts.entity.User; import org.springframework.stereotype.Component;
@Component public class ProfileMapper { public ProfileResponse toResponse(User u) {
    return new
            ProfileResponse(u.getId(),
            u.getFullName(),
            u.getDateOfBirth(),
            u.getGender(),
            u.getHeightCm(),
            u.getWeightKg(),
            u.getPreferredLanguage(),
            u.getProfileImageKey(),
            u.getStatus(),
            u.getCreatedAt(),
            u.getUpdatedAt());
}
public void update(User u, UpdateProfileRequest r) {
    u.setFullName(r.fullName().trim());u.setDateOfBirth(r.dateOfBirth());
    u.setGender(r.gender());u.setHeightCm(r.heightCm());
    u.setWeightKg(r.weightKg());
    u.setPreferredLanguage(r.preferredLanguage().trim());}
}
