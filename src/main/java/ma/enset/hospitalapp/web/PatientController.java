package ma.enset.hospitalapp.web;

import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping({"/index"})
    public String getpatient(Model model,
                             @RequestParam(name ="name",defaultValue = "0")int p,
                             @RequestParam(name = "size",defaultValue = "4")int s,
                             @RequestParam(name = "keyword",defaultValue = "")String kw) {
        Page<Patient> pagepatients = patientRepository.findAll(PageRequest.of(p,s));
        model.addAttribute("listpatients", pagepatients.getContent());
        model.addAttribute("pages",new int[pagepatients.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }


    @GetMapping("/delete")
    public String deletepatient(Long id) {

        patientRepository.deleteById(id);

        return "redirect:/index";
    }

}
