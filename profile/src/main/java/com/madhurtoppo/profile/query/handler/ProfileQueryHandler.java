package com.madhurtoppo.profile.query.handler;

import com.madhurtoppo.profile.dto.ProfileDto;
import com.madhurtoppo.profile.query.FindProfileQuery;
import com.madhurtoppo.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProfileQueryHandler {

    private final IProfileService iProfileService;


    @QueryHandler
    public ProfileDto findCustomer(FindProfileQuery findCustomerQuery) {
        return iProfileService.fetchProfile(findCustomerQuery.getMobileNumber());
    }

}
