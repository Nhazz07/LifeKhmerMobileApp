package com.lifekhmer.accounts.service.impl;

import com.lifekhmer.accounts.dto.request.UpdateProfileRequest; import com.lifekhmer.accounts.dto.response.ProfileResponse; import com.lifekhmer.accounts.entity.User; import com.lifekhmer.accounts.mapper.ProfileMapper; import com.lifekhmer.accounts.repository.UserRepository; import com.lifekhmer.accounts.service.UserService; import com.lifekhmer.common.exception.ResourceNotFoundException; import com.lifekhmer.security.CurrentUserService; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor public class UserServiceImpl implements UserService {
    private final UserRepository userRepository; private final ProfileMapper profileMapper; private final CurrentUserService currentUserService;
    @Override @Transactional(readOnly = true) public ProfileResponse getCurrentProfile() { return profileMapper.toResponse(current()); }
    @Override @Transactional public ProfileResponse updateCurrentProfile(UpdateProfileRequest request) { User user=current(); profileMapper.update(user,request); return profileMapper.toResponse(user); }
    private User current() { return userRepository.findById(currentUserService.getCurrentUserId()).orElseThrow(()->new ResourceNotFoundException("User profile not found")); }
}
