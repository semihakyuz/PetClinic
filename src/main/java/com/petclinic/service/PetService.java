package com.petclinic.service;

import java.util.List;
import java.util.Optional;

import com.petclinic.domain.Owner;
import com.petclinic.domain.Pet;

import org.springframework.data.domain.Page;

public interface PetService {

    public List<Pet> getAllPets();

    public Pet save(Pet pet);

    public void deletePet(Long id);

    public void delete(Pet animal);

    public void deleteById(Long id);

    public Optional<Pet> findPetById(Long id);

    public Pet updatePet(Pet newPet);

    public Pet updateAnimal(Pet currentAnimal, Pet newAnimalInfo, Owner user);

    public List<Pet> search(String name);

    public Page<Pet> findPaginated(int pageNo, int pageSize);


}
