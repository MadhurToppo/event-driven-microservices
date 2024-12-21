package com.madhurtoppo.profile.service.impl;

import com.madhurtoppo.profile.dto.ProfileDto;
import com.madhurtoppo.profile.entity.Profile;
import com.madhurtoppo.profile.exception.ResourceNotFoundException;
import com.madhurtoppo.profile.mapper.ProfileMapper;
import com.madhurtoppo.profile.repository.ProfileRepository;
import com.madhurtoppo.profile.service.IProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private ProfileRepository profileRepository;;


    @Override
    public ProfileDto fetchProfile(String mobileNumber) {
        Profile customer = profileRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Profile", "mobileNumber", mobileNumber)
        );
        return ProfileMapper.mapToCustomerDto(customer, new ProfileDto());
    }


}
