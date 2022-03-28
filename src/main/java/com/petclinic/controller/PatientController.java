package com.petclinic.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import com.petclinic.domain.PatientRegistration;
import com.petclinic.domain.Pet;
import com.petclinic.enums.TreatmentStatus;
import com.petclinic.domain.Owner;
import com.petclinic.service.OwnerService;
import com.petclinic.service.PatientService;
import com.petclinic.service.PetService;
import com.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PatientController {

    private final OwnerService ownerService;
    private final PetService petService;
    private final PatientService patientService;

    public PatientController(UserService userService, OwnerService ownerService, PetService petService,
            PatientService patientService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.patientService = patientService;
    }

    @GetMapping()
    public String getAllWaitingInline(Model model, Authentication auth) {

        List<PatientRegistration> patientRecord = patientService.findByStatus(TreatmentStatus.INLINE);

        model.addAttribute("owner", new Owner());
        model.addAttribute("pet", new Pet());
        model.addAttribute("records", patientRecord);
        return "home";
    }

    @GetMapping("/examination")
    public String getAllCompletedExamination(@RequestParam(value = "status", required = false) TreatmentStatus status,
            Model model, Authentication auth) {

        if (status != null) {

            if (status == TreatmentStatus.RELEASE) {

                List<PatientRegistration> patientRecords = patientService.findAllByReleaseDate(LocalDate.now());

                model.addAttribute("owner", new Owner());
                model.addAttribute("records", patientRecords);
                return "home";

            }

            List<PatientRegistration> patientRecords = patientService.findByStatus(status);

            model.addAttribute("owner", new Owner());
            model.addAttribute("records", patientRecords);
            return "home";

        }

        List<PatientRegistration> patientRecords = patientService.findByReleaseDateIsNotNull();

        model.addAttribute("owner", new Owner());
        model.addAttribute("records", patientRecords);
        return "home";

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addNewOwner(
            @ModelAttribute("owner") @Valid Owner owner,
            @ModelAttribute("pet") @Valid Pet patient, BindingResult result,
            String explanation,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {

            List<String> err = new ArrayList<>();
            result.getFieldErrors().forEach(e -> err.add(e.getDefaultMessage()));

            redirectAttributes.addFlashAttribute("userFieldsUpdateErrors", err);
            redirectAttributes.addFlashAttribute("fieldsErrorsOwnerObject", owner);
            redirectAttributes.addFlashAttribute("fieldsErrorsPetObject", patient);
            return "redirect:/";

        }

        if (owner.getOwnerId() != null) {

            Optional<Owner> foundedOwner = ownerService.findById(owner.getOwnerId());

            if (foundedOwner.isPresent()) {
                if (patient.getAnimalId() == null) {

                    patient.setOwner(foundedOwner.get());

                    Pet newPatient = petService.save(patient);
                    patientService.createNewPatientRecord(newPatient, explanation);

                    return "redirect:/";
                }

                Optional<Pet> foundedPatient = petService.findPetById(patient.getAnimalId());

                if (foundedPatient.isPresent()) {

                    patientService.createNewPatientRecord(foundedPatient.get(), explanation);

                    return "redirect:/";

                }

            }

        }

        Owner foundedOwner = ownerService.findByPhoneNumber(owner.getPhoneNumber());

        if (foundedOwner != null) {

            String foundedOwnerPhoneNumber = foundedOwner.getPhoneNumber();
            String requestPhoneNumber = owner.getPhoneNumber();

            if (foundedOwnerPhoneNumber.equals(requestPhoneNumber)) {

                redirectAttributes.addFlashAttribute("registerFailedOwnerMessage", owner.getPhoneNumber());
                return "redirect:/";

            }
        }

        Owner newOwner = ownerService.save(owner);

        patient.setOwner(newOwner);
        patient.setAnimalId(null);

        Pet newPatient = petService.save(patient);

        patientService.createNewPatientRecord(newPatient, explanation);

        redirectAttributes.addFlashAttribute("registerSuccessOwnerMessage", owner.getPhoneNumber());
        return "redirect:/";

    }

    @RequestMapping(value = "/examination/edit")
    public String editExaminationStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) TreatmentStatus requestStatus,
            String review,
            RedirectAttributes redirectAttributes) {

        try {

            Optional<PatientRegistration> existingRecord = patientService.findRecordById(id);

            if (existingRecord.isPresent()) {

                PatientRegistration editedRecord = patientService.editPatientRecord(requestStatus,
                        existingRecord.get(), review);

                TreatmentStatus newUrlParam = editedRecord.getStatus();

                redirectAttributes.addAttribute("status", newUrlParam);
                return "redirect:/examination";
            }

            redirectAttributes.addFlashAttribute("Kayıt bulunamadı!");
            return "redirect:/";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("Kayıt bulunamadı!");
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/examination/details", method = RequestMethod.GET)
    public String examinationDetail(Model model,
            @RequestParam(value = "id", required = false) Long id, RedirectAttributes redirectAttributes) {

        try {

            Optional<PatientRegistration> existingRecord = patientService.findRecordById(id);

            if (existingRecord.isPresent()) {

                model.addAttribute("examinationDetail", existingRecord.get());
                return "home";
            }

            redirectAttributes.addFlashAttribute("Kayıt bulunamadı!");
            return "redirect:/";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("Kayıt bulunamadı!");
            return "redirect:/";
        }

    }

    @RequestMapping(value = "examination/delete", method = RequestMethod.POST)
    public String deleteExaminationRecord(
            Long recordId,
            RedirectAttributes redirectAttributes,
            TreatmentStatus param) {

        try {

            patientService.delete(recordId);

            if (param == null) {

                return "redirect:/";

            }
            redirectAttributes.addAttribute("status", param);
            return "redirect:/examination";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("deleteFailedMessage", recordId);
            return "redirect:/examination";
        }

    }

}
