package com.petclinic.repository;

import java.util.List;
import com.petclinic.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByEmail(String email);

    List<Owner> findByFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(String firstName, String lastName,
            String phoneNumber);

    Owner findByPhoneNumber(String phoneNumber);

}
