package com.madhurtoppo.profile.mapper;


import com.madhurtoppo.profile.dto.ProfileDto;
import com.madhurtoppo.profile.entity.Profile;


public class ProfileMapper {

    public static ProfileDto mapToProfileDto(Profile profile, ProfileDto profileDto) {
        profileDto.setName(profile.getName());
        profileDto.setMobileNumber(profile.getMobileNumber());
        profileDto.setAccountNumber(profile.getAccountNumber());
        profileDto.setCardNumber(profile.getCardNumber());
        profileDto.setLoanNumber(profile.getLoanNumber());
        return profileDto;
    }

}
