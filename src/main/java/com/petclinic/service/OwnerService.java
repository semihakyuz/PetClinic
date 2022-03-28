package com.petclinic.service;

import java.util.List;
import java.util.Optional;

import com.petclinic.domain.Owner;

import org.springframework.data.domain.Page;

public interface OwnerService {

    public List<Owner> findAll();

    public Owner findByEmail(String email);

    public Optional<Owner> findById(Long id);

    public List<Owner> findOwner(String firstName, String lastName, String phoneNumber);

    public Owner save(Owner newOwner);

    public Owner findByPhoneNumber(String phoneNumber);

    public void update(Owner owner);

    public void deleteOwner(long id);

    public Page<Owner> findPaginated(int pageNo, int pageSize);

    public void updateOwner(Owner currentOwner, Owner newOwner, Long animalId);

}