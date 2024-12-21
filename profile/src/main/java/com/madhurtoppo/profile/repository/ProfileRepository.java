package com.madhurtoppo.profile.repository;

import com.madhurtoppo.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);


}
