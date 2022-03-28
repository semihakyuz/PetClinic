package com.petclinic.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import com.petclinic.domain.Pet;
import com.petclinic.domain.Owner;
import com.petclinic.service.OwnerService;
import com.petclinic.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping(value = "/pets")
@Controller
public class PetController {

    private final PetService petService;
    private final OwnerService ownerService;

    public PetController(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;
    }

    @GetMapping()
    public String getAllPets(Model model) {

        return findPaginated(1, model);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteAnimal(Long recordId, Long ownerId, RedirectAttributes redirectAttributes

    ) {

        try {

            long petId = recordId;

            Optional<Pet> existingAnimal = petService.findPetById(petId);

            if (existingAnimal.isPresent()) {

                petService.deleteById(existingAnimal.get().getAnimalId());

                return "redirect:/pets";

            }

            return "redirect:/pets";

        } catch (Exception e) {

            return "redirect:/pets";
        }

    }

    @GetMapping("/update")
    public String updatePet(
            Model model,
            @RequestParam(value = "id", required = false) Long id) {

        try {

            long petId = id;

            Optional<Pet> existingPet = petService.findPetById(petId);

            if (existingPet.isPresent()) {

                model.addAttribute("pet", new Pet());
                model.addAttribute("animalDetailUpdate", existingPet.get());
                return "home";

            }

            return "redirect:/pets";

        } catch (Exception e) {

            return "redirect:/pets";
        }

    }

    @PostMapping("/update/{id}")
    public String updatePet(
            @ModelAttribute("pet") @Valid Pet pet, @PathVariable Long id, Long ownerId,
            RedirectAttributes redirectAttributes, BindingResult result,
            Model model) {

        Long petId = id;

        if (petId == null) {

            return "redirect:/pets/";
        }

        try {

            Optional<Pet> foundedPet = petService.findPetById(petId);

            if (foundedPet.isPresent()) {

                if (ownerId != null) {

                    Optional<Owner> foundedOwner = ownerService.findById(ownerId);

                    if (foundedOwner.isPresent()) {

                        petService.updateAnimal(foundedPet.get(), pet, foundedOwner.get());

                        redirectAttributes.addAttribute("id", petId);
                        return "redirect:/pets/details";
                    }

                    petService.updateAnimal(foundedPet.get(), pet, null);

                    redirectAttributes.addAttribute("id", petId);
                    return "redirect:/pets/details";

                }

                petService.updateAnimal(foundedPet.get(), pet, null);

                redirectAttributes.addAttribute("id", petId);
                return "redirect:/pets/details";

            }

            return "redirect:/pets/";

        } catch (Exception e) {

            return "redirect:/pets/";
        }

    }

    @GetMapping("/search")
    public String getSearchResult(@RequestParam(value = "searchterm", required = false) String searchterm, Model model,
            RedirectAttributes redirectAttributes) {

        if (searchterm == null) {

            return "redirect:/pets";
        }
        ;

        List<Pet> searchResult = petService.search(searchterm);

        if (searchResult.isEmpty()) {

            redirectAttributes.addFlashAttribute("notFoundPetsMessage", searchterm);
            return "redirect:/pets";

        }

        model.addAttribute("pet", new Pet());
        model.addAttribute("foundedPetMessage",
                searchterm + "isminde" + searchResult.size() + "adet kayıt bulundu");
        model.addAttribute("allAnimals", searchResult);
        return "home";

    }

    @RequestMapping(value = "details", method = RequestMethod.GET)
    public String examinationDetail(Model model,
            @RequestParam(value = "id", required = false) Long id) {

        Long petId = id;

        try {

            Optional<Pet> existingPet = petService.findPetById(petId);

            if (existingPet.isPresent()) {

                model.addAttribute("animalDetail", existingPet.get());
                return "home";
            }

            return "redirect:/owner";

        } catch (Exception e) {

            return "redirect:/owner";
        }

    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {

        int pageSize = 6;

        Page<Pet> page = petService.findPaginated(pageNo, pageSize);
        List<Pet> allAnimals = page.getContent();

        model.addAttribute("pet", new Pet());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalAnimals", page.getTotalElements());
        model.addAttribute("allAnimals", allAnimals);
        return "home";
    }

}
