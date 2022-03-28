package com.petclinic.service;

import java.util.List;
import java.util.Optional;
import com.petclinic.domain.Owner;
import com.petclinic.repository.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetServiceImpl petServiceImpl;

    public OwnerServiceImpl(OwnerRepository ownerRepository, PetServiceImpl petServiceImpl) {
        this.ownerRepository = ownerRepository;
        this.petServiceImpl = petServiceImpl;

    }

    public List<Owner> findAll() {

        return ownerRepository.findAll();
    }

    public Owner findByEmail(String email) {

        return ownerRepository.findByEmail(email);
    }

    public Optional<Owner> findById(Long id) {

        return ownerRepository.findById(id);
    }

    public List<Owner> findOwner(String firstName,
            String lastName, String phoneNumber) {

        return ownerRepository.findByFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(firstName, lastName,
                phoneNumber);
    }

    public Owner save(Owner newOwner) {

        return ownerRepository.save(newOwner);
    }

    public Owner findByPhoneNumber(String phoneNumber) {

        return ownerRepository.findByPhoneNumber(phoneNumber);
    }

    public void update(Owner owner) {
        ownerRepository.save(owner);

    }

    public void updateOwner(Owner currentOwner, Owner newOwner, Long animalId) {

        if (animalId == null) {
            currentOwner.setFirstName(newOwner.getFirstName());
            currentOwner.setLastName(newOwner.getLastName());
            currentOwner.setAddress(newOwner.getAddress());
            currentOwner.setPhoneNumber(newOwner.getPhoneNumber());
            currentOwner.setEmail(newOwner.getEmail());

            ownerRepository.save(currentOwner);

        } else {

            petServiceImpl.deleteById(animalId);
        }

    }

    public void deleteOwner(long id) {

        ownerRepository.deleteById(id);
    }

    @Override
    public Page<Owner> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return ownerRepository.findAll(pageable);
    }

}
