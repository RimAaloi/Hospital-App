package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping({"/user/index"})
    public String getpatient(Model model,
                             @RequestParam(name ="page",defaultValue = "0")int p,
                             @RequestParam(name = "size",defaultValue = "4")int s,
                             @RequestParam(name = "keyword",defaultValue = "")String kw) {
        Page<Patient> pagepatients;
        // Si le mot-clé est vide, récupérer tous les patients
        if (kw.isEmpty()) {
            pagepatients = patientRepository.findAll(PageRequest.of(p, s));
        } else {
            // Utiliser la méthode chercher pour rechercher par nom
            pagepatients = patientRepository.chercher(kw, PageRequest.of(p, s));
        }
        model.addAttribute("listpatients", pagepatients.getContent());
        model.addAttribute("pages",new int[pagepatients.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/delete")
    public String eletepatient(@RequestParam Long id,
                                @RequestParam String keyword,
                                @RequestParam int page) {
        patientRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/editPatient")
    public String editPatients(Model model,Long id,String keyword,int page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("patient introvable");
        model.addAttribute("patient", patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "editpatient";
    }



    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

}
