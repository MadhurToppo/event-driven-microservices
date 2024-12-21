package com.madhurtoppo.profile.query.projection;

import com.madhurtoppo.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class ProfileProjection {

    private final IProfileService iProfileService;


}
