package com.madhurtoppo.profile.service.impl;

import com.madhurtoppo.common.event.AccountDataChangedEvent;
import com.madhurtoppo.common.event.CardDataChangeEvent;
import com.madhurtoppo.common.event.CustomerDataChangedEvent;
import com.madhurtoppo.common.event.LoanDataChangeEvent;
import com.madhurtoppo.profile.constants.ProfileConstants;
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

    private ProfileRepository profileRepository;


    @Override
    public void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent) {
        Profile profile = profileRepository
                .findByMobileNumberAndActiveSw(customerDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                .orElseGet(Profile::new);
        profile.setMobileNumber(customerDataChangedEvent.getMobileNumber());
        if (customerDataChangedEvent.getName() != null) {
            profile.setName(customerDataChangedEvent.getName());
        }
        profile.setActiveSw(customerDataChangedEvent.isActiveSw());
        profileRepository.save(profile);
    }


    @Override
    public void handleAccountDataChangedEvent(AccountDataChangedEvent accountDataChangedEvent) {
        Profile profile = profileRepository
                .findByMobileNumberAndActiveSw(accountDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", accountDataChangedEvent.getMobileNumber()));
        profile.setAccountNumber(accountDataChangedEvent.getAccountNumber());
        profileRepository.save(profile);
    }


    @Override
    public void handleLoanDataChangedEvent(LoanDataChangeEvent loanDataChangeEvent) {
        Profile profile = profileRepository
                .findByMobileNumberAndActiveSw(loanDataChangeEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", loanDataChangeEvent.getMobileNumber()));
        profile.setLoanNumber(loanDataChangeEvent.getLoanNumber());
        profileRepository.save(profile);
    }


    @Override
    public void handleCardDataChangedEvent(CardDataChangeEvent cardDataChangeEvent) {
        Profile profile = profileRepository
                .findByMobileNumberAndActiveSw(cardDataChangeEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", cardDataChangeEvent.getMobileNumber()));
        profile.setCardNumber(cardDataChangeEvent.getCardNumber());
        profileRepository.save(profile);
    }


    @Override
    public ProfileDto fetchProfile(String mobileNumber) {
        Profile profile = profileRepository
                .findByMobileNumberAndActiveSw(mobileNumber, true)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", mobileNumber));
        return ProfileMapper.mapToProfileDto(profile, new ProfileDto());
    }


}
