package com.skillsync.user.service;

import com.skillsync.user.dto.ChangePasswordRequest;
import com.skillsync.user.dto.UpdateProfileRequest;
import com.skillsync.user.dto.UserProfileDTO;

public interface UserService {

    UserProfileDTO getProfile();

    UserProfileDTO updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);
}
