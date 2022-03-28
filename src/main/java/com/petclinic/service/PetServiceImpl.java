package com.petclinic.service;

import java.util.List;
import java.util.Optional;
import com.petclinic.domain.Pet;
import com.petclinic.domain.Owner;
import com.petclinic.repository.PetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {

        return petRepository.findAll();
    }

    public Pet save(Pet pet) {

        return petRepository.save(pet);

    }

    public void deletePet(Long id) {

        petRepository.deleteById(id);

    };

    public void delete(Pet animal) {

        petRepository.delete(animal);

    };

    public void deleteById(Long id) {

        petRepository.deleteById(id);

    };

    public Optional<Pet> findPetById(Long id) {

        return petRepository.findById(id);
    }

    public Pet updatePet(Pet newPet) {

        return petRepository.save(newPet);
    };

    public Pet updateAnimal(Pet currentAnimal, Pet newAnimalInfo, Owner user) {

        if (user != null) {

            currentAnimal.setName(newAnimalInfo.getName());
            currentAnimal.setAge(newAnimalInfo.getAge());
            currentAnimal.setBreed(newAnimalInfo.getBreed());
            currentAnimal.setType(newAnimalInfo.getType());
            currentAnimal.setOwner(user);

            return petRepository.save(currentAnimal);

        } else {

            currentAnimal.setName(newAnimalInfo.getName());
            currentAnimal.setAge(newAnimalInfo.getAge());
            currentAnimal.setBreed(newAnimalInfo.getBreed());
            currentAnimal.setType(newAnimalInfo.getType());

            return petRepository.save(currentAnimal);
        }

    };

    public List<Pet> search(String name) {

        return petRepository.findByNameContainingIgnoreCase(name);
    }

    public Page<Pet> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return petRepository.findAll(pageable);
    }

}
