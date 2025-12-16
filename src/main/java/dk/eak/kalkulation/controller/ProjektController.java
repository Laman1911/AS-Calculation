package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.DelProjekt;
import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.DelProjektRepository;
import dk.eak.kalkulation.service.ProjektService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projekter")
public class ProjektController {

    private final ProjektService projektService;
    private final DelProjektRepository delProjektRepository;

    public ProjektController(ProjektService projektService,
                             DelProjektRepository delProjektRepository) {
        this.projektService = projektService;
        this.delProjektRepository = delProjektRepository;
    }

    // 🔹 ALL PROJECTS
    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("projekts", projektService.getAll());
        return "projekter";
    }

    // 🔹 PROJECT DETAILS + DELPROJEKTER
    @GetMapping("/{id}")
    public String details(@PathVariable int id, Model model) {

        Projekt project = projektService.getById(id);
        List<DelProjekt> delprojekter =
                delProjektRepository.findByProjektId(id);

        model.addAttribute("project", project);
        model.addAttribute("delprojekter", delprojekter);

        return "project-details";
    }

    // 🔹 CREATE PROJECT
    @GetMapping("/opret")
    public String createForm(Model model) {
        model.addAttribute("projekt", new Projekt());
        return "opret_projekt";
    }

    @PostMapping("/opret")
    public String create(@ModelAttribute Projekt projekt) {
        projektService.create(projekt);
        return "redirect:/projekter";
    }

    // 🔹 EDIT PROJECT
    @GetMapping("/rediger/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("projekt", projektService.getById(id));
        return "rediger_projekt";
    }

    @PostMapping("/rediger")
    public String edit(@ModelAttribute Projekt projekt) {
        projektService.update(projekt);
        return "redirect:/projekter";
    }

    // 🔹 DELETE PROJECT
    @GetMapping("/slet/{id}")
    public String delete(@PathVariable int id) {
        projektService.delete(id);
        return "redirect:/projekter";
    }
}
