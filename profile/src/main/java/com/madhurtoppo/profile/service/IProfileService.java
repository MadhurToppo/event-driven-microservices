package com.madhurtoppo.profile.service;

import com.madhurtoppo.profile.dto.ProfileDto;


public interface IProfileService {


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Profile Details based on a given mobileNumber
     */
    ProfileDto fetchProfile(String mobileNumber);


}
