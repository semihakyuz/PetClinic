package com.petclinic.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import com.petclinic.domain.Owner;
import com.petclinic.domain.Pet;
import com.petclinic.service.OwnerService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
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

@Controller
@RequestMapping(value = "/owner")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;

    }

    @GetMapping()
    public String getAllOwners(Model model, Authentication auth) {

        return findPaginated(1, model);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteAnimal(Long recordId, RedirectAttributes redirectAttributes

    ) {
        Long ownerId = recordId;

        try {

            ownerService.deleteOwner(ownerId);

            redirectAttributes.addFlashAttribute("deleteSuccessMessage", ownerId);
            return "redirect:/owner";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("deleteFailedMessage", ownerId);
            return "redirect:/owner";
        }

    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {

        int pageSize = 6;

        Page<Owner> page = ownerService.findPaginated(pageNo, pageSize);
        List<Owner> allOwners = page.getContent();

        model.addAttribute("pet", new Pet());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUsers", page.getTotalElements());
        model.addAttribute("allOwners", allOwners);

        return "home";
    }

    @RequestMapping(value = "details", method = RequestMethod.GET)
    public String examinationDetail(Model model,
            @RequestParam(value = "id", required = false) Long id) {

        Optional<Owner> existingOwner = ownerService.findById(id);

        if (existingOwner.isPresent()) {

            model.addAttribute("ownerDetail", existingOwner.get());
            return "home";
        }

        return "redirect:/owner";

    }

    @GetMapping("/search")
    public String getSearchResult(
            @RequestParam(value = "searchterm", required = false) String searchterm,
            @RequestParam(value = "ownersearchterm", required = false) String ownersearchterm,
            @RequestParam(value = "phone", required = false) String phoneTerm,
            @RequestParam(value = "petId", required = false) Long petId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (phoneTerm != null) {

            Owner foundedOwner = ownerService.findByPhoneNumber(phoneTerm);

            if (foundedOwner != null) {

                redirectAttributes.addFlashAttribute("foundedOwner", foundedOwner);
                return "redirect:/";

            }

            redirectAttributes.addFlashAttribute("phone", phoneTerm);
            return "redirect:/";

        }

        if (ownersearchterm != null) {

            if (petId != null) {

                List<Owner> foundedOwners = ownerService.findOwner(ownersearchterm, ownersearchterm, ownersearchterm);

                if (foundedOwners.isEmpty()) {

                    redirectAttributes.addAttribute("id", petId);
                    redirectAttributes.addFlashAttribute("notfound", true);
                    return "redirect:/pets/update";

                }

                redirectAttributes.addAttribute("id", petId);
                redirectAttributes.addFlashAttribute("listOwners", foundedOwners);
                return "redirect:/pets/update";

            }
        }

        if (searchterm == "") {

            redirectAttributes.addAttribute("page", 1);
            return "redirect:/owner";
        }
        ;

        List<Owner> searchResult = ownerService.findOwner(searchterm, searchterm, searchterm);

        if (searchResult.isEmpty()) {

            redirectAttributes.addFlashAttribute("notFoundOwnerMessage", searchterm);
            return "redirect:/owner";

        }

        model.addAttribute("owner", new Owner());
        model.addAttribute("foundedOwnerMessage",
                searchterm + "isminde" + searchResult.size() + "adet kayıt bulundu");
        model.addAttribute("allOwners", searchResult);

        return "home";

    }

    @GetMapping("/update")
    public String updatePet(
            @RequestParam(value = "id", required = false) Long id, RedirectAttributes redirectAttributes, Model model) {

        try {

            Long existingOwnerId = id;

            Optional<Owner> existingOwner = ownerService.findById(existingOwnerId);

            if (existingOwner.isPresent()) {

                model.addAttribute("pet", new Pet());
                model.addAttribute("ownerDetailUpdate", existingOwner.get());
                return "home";

            }

            redirectAttributes.addFlashAttribute("failedOwnerUpdateMessage", "Hayvan sahibi bulunamadı.");
            return "redirect:/owner";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("failedOwnerUpdateMessage", "Hayvan sahibi bulunamadı.");
            return "redirect:/owner";
        }

    }

    @PostMapping("/update/{id}")
    public String updatePet(@ModelAttribute("owner") @Valid Owner owner, @PathVariable Long id, Long animalId,
            RedirectAttributes redirectAttributes, BindingResult result,
            Model model) {

        Long existingOwnerId = id;
        Long existingPetId = animalId;

        try {

            Optional<Owner> existingOwner = ownerService.findById(existingOwnerId);

            if (existingOwner.isPresent()) {

                if (existingPetId != null) {

                    ownerService.updateOwner(existingOwner.get(), owner, existingPetId);

                    redirectAttributes.addFlashAttribute("successOwnerUpdateMessage",
                            "Hayvan sahibi ve sahip olduğu hayvan bilgisi güncelledindi.");
                    return "redirect:/owner";

                }

                ownerService.updateOwner(existingOwner.get(), owner, null);

                redirectAttributes.addFlashAttribute("successOwnerUpdateMessage",
                        "Hayvan sahibi ve sahip olduğu hayvan bilgisi güncelledindi.");
                return "redirect:/owner";
            }

            redirectAttributes.addFlashAttribute("failedOwnerUpdateMessage", "Hayvan sahibi bulunamadı.");
            return "redirect:/owner";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("failedOwnerUpdateMessage", "Hayvan sahibi bulunamadı.");
            return "redirect:/owner";
        }

    }

};
